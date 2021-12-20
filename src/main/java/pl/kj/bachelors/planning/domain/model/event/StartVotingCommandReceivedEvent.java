package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class StartVotingCommandReceivedEvent extends ApplicationEvent {
    public StartVotingCommandReceivedEvent(Object source) {
        super(source);
    }
}
