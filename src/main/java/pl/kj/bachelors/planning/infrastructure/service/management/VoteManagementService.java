package pl.kj.bachelors.planning.infrastructure.service.management;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.entity.Vote;
import pl.kj.bachelors.planning.domain.model.event.MemberVotedEvent;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.service.management.VoteManager;
import pl.kj.bachelors.planning.infrastructure.repository.VoteRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class VoteManagementService extends BaseManagementService implements VoteManager {
    private final VoteRepository repository;

    @Autowired
    public VoteManagementService(VoteRepository repository, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void vote(PlanningItem item, String userId, Estimation value) throws ApiError {
        this.ensureThatStatusIsCorrect(item.getPlanning(), List.of(PlanningStatus.VOTING), "PL.131");
        this.ensureThatItemHasFocus(item, "PL.132");
        this.ensureThatUserCanVote(item, userId, "PL.133");

        Vote vote = new Vote();
        vote.setUserId(userId);
        vote.setValue(value);
        vote.setPlanningItem(item);

        this.repository.save(vote);
        this.publishEvent(new MemberVotedEvent(
                this,
                item.getPlanning().getId(),
                item.getId(),
                userId
        ));
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void clearVotes(PlanningItem item) throws ApiError {
        this.ensureThatStatusIsCorrect(
                item.getPlanning(),
                Arrays.asList(PlanningStatus.PROGRESSING, PlanningStatus.VOTING),
                "PL.134"
        );
        this.ensureThatItemHasFocus(item, "PL.135");

        this.repository.deleteByPlanningItem(item);
    }

    private void ensureThatStatusIsCorrect(Planning planning, List<PlanningStatus> allowed, String errorCode)
            throws ApiError {
        if(!planning.hasStatusIn(allowed)) {
            throw new ApiError("", errorCode, null);
        }
    }

    private void ensureThatItemHasFocus(PlanningItem item, String errorCode) throws ApiError {
        if(!item.isFocused()) {
            throw new ApiError("", errorCode, null);
        }

    }

    private void ensureThatUserCanVote(PlanningItem item, String userId, String errorCode) throws ApiError {
        if(item.getVotes().stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new ApiError("", errorCode, null);
        }
    }
}
