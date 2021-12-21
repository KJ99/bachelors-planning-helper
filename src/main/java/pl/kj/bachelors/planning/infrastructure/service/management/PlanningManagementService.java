package pl.kj.bachelors.planning.infrastructure.service.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.config.ApiConfig;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.event.PlanningCompletedEvent;
import pl.kj.bachelors.planning.domain.model.event.VotingStatusChangedEvent;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.service.management.PlanningManager;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

import java.util.List;

@Service
public class PlanningManagementService extends BaseManagementService implements PlanningManager {
    private final PlanningRepository repository;
    private final ApiConfig apiConfig;

    @Autowired
    public PlanningManagementService(
            PlanningRepository repository,
            ApplicationEventPublisher eventPublisher,
            ApiConfig apiConfig) {
        super(eventPublisher);
        this.repository = repository;
        this.apiConfig = apiConfig;
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void open(Planning planning) throws ApiError {
        if(!planning.getStatus().equals(PlanningStatus.SCHEDULED)) {
            throw new ApiError(this.apiConfig.getErrors().get("PL.005"), "PL.005", null);
        }
        planning.setStatus(PlanningStatus.PROGRESSING);

        this.repository.save(planning);
    }

    @Override
    @Transactional(rollbackFor = ApiError.class)
    public void close(Planning planning) throws ApiError {
        if(!planning.getStatus().equals(PlanningStatus.PROGRESSING) && !planning.getStatus().equals(PlanningStatus.VOTING)) {
            throw new ApiError(this.apiConfig.getErrors().get("PL.006"), "PL.006", null);
        }

        planning.setStatus(PlanningStatus.FINISHED);

        this.repository.save(planning);
        this.publishEvent(new PlanningCompletedEvent(this, planning.getId()));
    }

    @Override
    public void enableVoting(Planning planning) throws ApiError {
        if(!planning.hasStatusIn(List.of(PlanningStatus.PROGRESSING))) {
            throw new ApiError(this.apiConfig.getErrors().get("PL.010"), "PL.010", null);
        }
        planning.setStatus(PlanningStatus.VOTING);
        this.repository.save(planning);
        this.publishEvent(new VotingStatusChangedEvent(this, planning.getId(), true));
    }

    @Override
    public void disableVoting(Planning planning) throws ApiError {
        if(!planning.hasStatusIn(List.of(PlanningStatus.VOTING))) {
            throw new ApiError(this.apiConfig.getErrors().get("PL.011"), "PL.011", null);
        }
        planning.setStatus(PlanningStatus.PROGRESSING);
        this.repository.save(planning);
        this.publishEvent(new VotingStatusChangedEvent(this, planning.getId(), false));
    }
}
