package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class FocusNextCommandReceivedEvent extends ApplicationEvent {
    public FocusNextCommandReceivedEvent(Object source) {
        super(source);
    }
}
