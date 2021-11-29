package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public class ItemEstimatedEvent extends ApplicationEvent {
    private final int planningId;
    private final int itemId;
    private final Estimation estimation;

    public ItemEstimatedEvent(Object source, int planningId, int itemId, Estimation estimation) {
        super(source);
        this.planningId = planningId;
        this.itemId = itemId;
        this.estimation = estimation;
    }

    public int getPlanningId() {
        return planningId;
    }

    public int getItemId() {
        return itemId;
    }

    public Estimation getEstimation() {
        return estimation;
    }
}
