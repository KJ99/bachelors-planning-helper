package pl.kj.bachelors.planning.domain.model.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;

@JsonNaming(value = PropertyNamingStrategies.KebabCaseStrategy.class)
public class PlanningSearchModel {
    private PlanningStatus status;

    public PlanningStatus getStatus() {
        return status;
    }

    public void setStatus(PlanningStatus status) {
        this.status = status;
    }
}
