package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;

public class MemberVotedEvent extends ApplicationEvent {
    private final int planningId;
    private final int itemId;
    private final String userId;

    public MemberVotedEvent(Object source, int planningId, int itemId, String userId) {
        super(source);
        this.planningId = planningId;
        this.itemId = itemId;
        this.userId = userId;
    }

    public int getPlanningId() {
        return planningId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }
}
