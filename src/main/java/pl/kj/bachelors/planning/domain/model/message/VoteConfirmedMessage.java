package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;

public class VoteConfirmedMessage extends Message<Object> {
    public VoteConfirmedMessage() {
        super(MessageType.VOTE_CONFIRMED);
    }
}
