package pl.kj.bachelors.planning.infrastructure.service.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.config.ApiConfig;
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
    private final ApiConfig apiConfig;

    @Autowired
    public VoteManagementService(VoteRepository repository, ApplicationEventPublisher eventPublisher, ApiConfig apiConfig) {
        super(eventPublisher);
        this.repository = repository;
        this.apiConfig = apiConfig;
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void vote(PlanningItem item, String userId, Estimation value) throws ApiError {
        this.ensureThatStatusIsCorrect(item.getPlanning(), List.of(PlanningStatus.VOTING), "PL.012");
        this.ensureThatItemHasFocus(item, "PL.013");
        this.ensureThatUserCanVote(item, userId, "PL.014");

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
                "PL.015"
        );
        this.ensureThatItemHasFocus(item, "PL.016");

        this.repository.deleteByPlanningItem(item);
    }

    private void ensureThatStatusIsCorrect(Planning planning, List<PlanningStatus> allowed, String errorCode)
            throws ApiError {
        if(!planning.hasStatusIn(allowed)) {
            throw new ApiError(this.apiConfig.getErrors().get(errorCode), errorCode, null);
        }
    }

    private void ensureThatItemHasFocus(PlanningItem item, String errorCode) throws ApiError {
        if(!item.isFocused()) {
            throw new ApiError(this.apiConfig.getErrors().get(errorCode), errorCode, null);
        }

    }

    private void ensureThatUserCanVote(PlanningItem item, String userId, String errorCode) throws ApiError {
        if(item.getVotes().stream().anyMatch(vote -> vote.getUserId().equals(userId))) {
            throw new ApiError(this.apiConfig.getErrors().get(errorCode), errorCode, null);
        }
    }
}
