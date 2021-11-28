package pl.kj.bachelors.planning.domain.service.management;

import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public interface VoteManager {
    void vote(PlanningItem item, String userId, Estimation value) throws ApiError;
    void clearVotes(PlanningItem item) throws ApiError;
}
