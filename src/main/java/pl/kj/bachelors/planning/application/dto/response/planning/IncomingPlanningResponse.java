package pl.kj.bachelors.planning.application.dto.response.planning;

import pl.kj.bachelors.planning.domain.model.entity.Planning;

public class IncomingPlanningResponse {
    private boolean scheduled;
    private PlanningResponse data;

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public PlanningResponse getData() {
        return data;
    }

    public void setData(PlanningResponse data) {
        this.data = data;
    }
}
