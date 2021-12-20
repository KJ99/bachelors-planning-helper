package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class StopVotingCommandReceivedEvent extends ApplicationEvent {
    public StopVotingCommandReceivedEvent(Object source) {
        super(source);
    }
}
