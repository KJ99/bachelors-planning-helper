package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class MemberHasJoinEvent extends ApplicationEvent {
    private final String userId;
    private final Integer planningId;

    public MemberHasJoinEvent(Object source, String userId, Integer planningId) {
        super(source);
        this.userId = userId;
        this.planningId = planningId;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getPlanningId() {
        return planningId;
    }
}
