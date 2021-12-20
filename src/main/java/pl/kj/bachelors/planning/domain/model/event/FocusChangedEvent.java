package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class FocusChangedEvent extends ApplicationEvent {
    private final Integer planningId;
    private final int itemId;

    public FocusChangedEvent(Object source, Integer planningId, Integer itemId) {
        super(source);
        this.planningId = planningId;
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public int getPlanningId() {
        return planningId;
    }
}
