package pl.kj.bachelors.planning.domain.model.command;

import pl.kj.bachelors.planning.domain.model.extension.CommandType;

public class Command<T> {
    protected CommandType type;
    protected T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }
}
