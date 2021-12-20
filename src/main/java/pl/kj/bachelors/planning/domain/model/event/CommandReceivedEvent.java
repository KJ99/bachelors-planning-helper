package pl.kj.bachelors.planning.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class CommandReceivedEvent<T> extends ApplicationEvent {
    private final T payload;

    public CommandReceivedEvent(Object source, T payload) {
        super(source);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
