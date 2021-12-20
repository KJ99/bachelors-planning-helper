package pl.kj.bachelors.planning.application.listener;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.event.*;
import pl.kj.bachelors.planning.domain.model.message.*;
import pl.kj.bachelors.planning.domain.model.payload.PlanningItemPayload;
import pl.kj.bachelors.planning.domain.model.payload.VotePayload;
import pl.kj.bachelors.planning.domain.service.management.FocusManager;
import pl.kj.bachelors.planning.domain.service.management.PlanningManager;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.repository.VoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanningEventListener {
    private final SimpMessagingTemplate template;
    private final FocusManager focusManager;
    private final PlanningManager planningManager;
    private final PlanningRepository planningRepository;
    private final VoteRepository voteRepository;
    private final PlanningItemRepository itemRepository;
    private final ModelMapper mapper;

    @Autowired
    public PlanningEventListener(
            SimpMessagingTemplate template,
            FocusManager focusManager,
            PlanningManager planningManager,
            PlanningRepository planningRepository,
            VoteRepository voteRepository,
            PlanningItemRepository itemRepository,
            ModelMapper mapper) {
        this.template = template;
        this.focusManager = focusManager;
        this.planningManager = planningManager;
        this.planningRepository = planningRepository;
        this.voteRepository = voteRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    @EventListener
    public void handleFocusChangedEvent(FocusChangedEvent event) {
        PlanningItemPayload payload = new PlanningItemPayload();
        payload.setId(event.getItemId());
        ItemFocusedMessage message = new ItemFocusedMessage(payload);

        this.template.convertAndSend(String.format("/status/%d", event.getPlanningId()), message);
    }

    @EventListener
    @Transactional
    public void handleItemEstimatedEvent(ItemEstimatedEvent event) throws ApiError {
        PlanningItemPayload payload = new PlanningItemPayload();
        payload.setId(event.getItemId());
        payload.setEstimation(event.getEstimation());

        ItemEstimatedMessage message = new ItemEstimatedMessage(payload);
        this.template.convertAndSend(String.format("/estimations/%d", event.getPlanningId()), message);

        Planning planning = this.planningRepository.findById(event.getPlanningId()).orElseThrow();
        if(this.focusManager.hasNext(planning)) {
            this.focusManager.focusNext(planning);
            this.planningManager.enableVoting(planning);
        } else {
            this.planningManager.close(planning);
        }
    }

    @EventListener
    public void handleMemberVotedEvent(MemberVotedEvent event) {
        VoteConfirmedMessage confirmedMessage = new VoteConfirmedMessage();
        MemberVotedMessage votedMessage = new MemberVotedMessage(event.getUserId());

        this.template.convertAndSend(
                String.format("/member-votes/%d/%s", event.getPlanningId(), event.getUserId()),
                confirmedMessage
        );
        this.template.convertAndSend(String.format("/votes/%d", event.getPlanningId()), votedMessage);
    }

    @EventListener
    @Transactional
    public void handleVotingStatusChangedEvent(VotingStatusChangedEvent event) {
        VotingStatusMessage message = new VotingStatusMessage(event.isEnabled());
        this.template.convertAndSend(String.format("/status/%d", event.getPlanningId()), message);
        if(!event.isEnabled()) {
            Planning planning = this.planningRepository.findById(event.getPlanningId()).orElseThrow();
            PlanningItem item = this.itemRepository.findFirstByPlanningAndFocused(planning, true).orElseThrow();
            List<VotePayload> votes = this.voteRepository.findByPlanningItem(item)
                    .stream().map(vote -> new VotePayload(vote.getUserId(), vote.getValue()))
                    .collect(Collectors.toList());

            VotesListMessage votesListMessage = new VotesListMessage(votes);
            this.template.convertAndSend(String.format("/votes/%d", event.getPlanningId()), votesListMessage);
        }
    }

    @EventListener
    public void planningCompletedEvent(PlanningCompletedEvent event) {
        PlanningCompletedMessage message = new PlanningCompletedMessage();
        this.template.convertAndSend(String.format("/status/%d", event.getPlanningId()), message);
    }
}
