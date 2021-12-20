package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class PlanningCompletedEvent extends ApplicationEvent {
    private final int planningId;
    
    public PlanningCompletedEvent(Object source, int planningId) {
        super(source);
        this.planningId = planningId;
    }

    public int getPlanningId() {
        return planningId;
    }
}
