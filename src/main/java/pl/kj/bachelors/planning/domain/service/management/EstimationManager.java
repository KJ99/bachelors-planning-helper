package pl.kj.bachelors.planning.domain.service.management;

import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;

public interface EstimationManager {
    void setEstimation(PlanningItem item, int estimation) throws ApiError;
}
