package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class ResetVotesCommandReceivedEvent extends ApplicationEvent {
    public ResetVotesCommandReceivedEvent(Object source) {
        super(source);
    }
}
