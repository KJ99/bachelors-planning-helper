package pl.kj.bachelors.planning.domain.service.management;

import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;

public interface FocusManager {
    void focus(PlanningItem item) throws ApiError;
    void focusNext(Planning planning) throws ApiError;
    boolean hasNext(Planning planning);
}
