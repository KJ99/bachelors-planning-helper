package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class VotingStatusChangedEvent extends ApplicationEvent {
    private final int planningId;
    private final boolean enabled;

    public VotingStatusChangedEvent(Object source, int planningId, boolean enabled) {
        super(source);
        this.planningId = planningId;
        this.enabled = enabled;
    }

    public int getPlanningId() {
        return planningId;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
