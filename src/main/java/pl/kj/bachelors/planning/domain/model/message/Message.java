package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;

import java.util.Calendar;

public class Message<T> {
    private MessageType type;
    private Calendar createdAt;
    private T payload;

    public Message(MessageType type, T payload) {
        this.type = type;
        this.payload = payload;
        this.createdAt = Calendar.getInstance();
    }

    public Message(MessageType type) {
        this(type, null);
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
