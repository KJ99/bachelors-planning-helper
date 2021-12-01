package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class RunPlanningCommandReceivedEvent extends ApplicationEvent {
    public RunPlanningCommandReceivedEvent(Object source) {
        super(source);
    }
}
