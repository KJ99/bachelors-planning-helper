package pl.kj.bachelors.planning.domain.service.management;

import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;

public interface PlanningManager {
    void open(Planning planning) throws ApiError;
    void close(Planning planning) throws ApiError;
    void changeVotingStatus(boolean enabled) throws ApiError;
}
