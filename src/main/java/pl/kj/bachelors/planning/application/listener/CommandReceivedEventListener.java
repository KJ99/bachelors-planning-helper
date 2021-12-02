package pl.kj.bachelors.planning.application.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.event.*;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;
import pl.kj.bachelors.planning.domain.model.payload.EstimationCommandPayload;
import pl.kj.bachelors.planning.domain.service.management.EstimationManager;
import pl.kj.bachelors.planning.domain.service.management.FocusManager;
import pl.kj.bachelors.planning.domain.service.management.PlanningManager;
import pl.kj.bachelors.planning.domain.service.management.VoteManager;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

@Component
public class CommandReceivedEventListener {
    private final PlanningRepository planningRepository;
    private final PlanningItemRepository itemRepository;
    private final FocusManager focusManager;
    private final VoteManager voteManager;
    private final PlanningManager planningManager;
    private final EstimationManager estimationManager;

    @Autowired
    public CommandReceivedEventListener(
            PlanningRepository planningRepository,
            PlanningItemRepository itemRepository,
            FocusManager focusManager,
            VoteManager voteManager,
            PlanningManager planningManager,
            EstimationManager estimationManager) {
        this.planningRepository = planningRepository;
        this.itemRepository = itemRepository;
        this.focusManager = focusManager;
        this.voteManager = voteManager;
        this.planningManager = planningManager;
        this.estimationManager = estimationManager;
    }

    @EventListener
    @Transactional
    public void handleRunPlanningCommand(RunPlanningCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        this.focusManager.focusNext(this.getPlanning());
        this.planningManager.enableVoting(this.getPlanning());
    }

    @EventListener
    @Transactional
    public void handleFocusCommand(FocusCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        this.focusManager.focus(this.getPlanningItem(event.getPayload().getItemId()));

    }

    @EventListener
    @Transactional
    public void handleFocusNextCommand(FocusNextCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        this.focusManager.focusNext(this.getPlanning());

    }

    @EventListener
    @Transactional
    public void handleStartVotingCommand(StartVotingCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        this.planningManager.enableVoting(this.getPlanning());

    }

    @EventListener
    @Transactional
    public void handleVoteCommand(VoteCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        EstimationCommandPayload payload = event.getPayload();
        this.voteManager.vote(this.getPlanningItem(payload.getItemId()), this.getUid(), payload.getEstimation());

    }

    @EventListener
    @Transactional
    public void handleStopVotingCommand(StopVotingCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        this.planningManager.disableVoting(this.getPlanning());

    }

    @EventListener
    @Transactional
    public void handleResetVotesCommand(ResetVotesCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        Planning planning = this.getPlanning();
        PlanningItem item = this.itemRepository
                .findFirstByPlanningAndFocused(planning, true)
                .orElseThrow();
        this.voteManager.clearVotes(item);
        this.planningManager.enableVoting(planning);
    }

    @EventListener
    @Transactional
    public void handleEstimateCommand(EstimateCommandReceivedEvent event) throws ResourceNotFoundException, ApiError {
        EstimationCommandPayload payload = event.getPayload();
        this.estimationManager.setEstimation(this.getPlanningItem(payload.getItemId()), payload.getEstimation());
    }

    private Planning getPlanning() throws ResourceNotFoundException {
        int id = RequestHandler.getAttribute(RequestAttributeName.PLANNING_ID.value, Integer.class).orElseThrow();

        return this.planningRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    private PlanningItem getPlanningItem(int id) throws ResourceNotFoundException {
        return this.itemRepository.findFirstByIdAndPlanning(id, this.getPlanning())
                .orElseThrow(ResourceNotFoundException::new);
    }

    private String getUid() {
        return RequestHandler.getCurrentUserId().orElseThrow();
    }
}
