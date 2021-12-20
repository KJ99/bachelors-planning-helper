package pl.kj.bachelors.planning.domain.service.management;

import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public interface EstimationManager {
    void setEstimation(PlanningItem item, Estimation estimation) throws ApiError;
}
