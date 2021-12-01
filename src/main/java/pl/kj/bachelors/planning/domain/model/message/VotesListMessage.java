package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.message.VotePayload;

import java.util.List;

public class VotesListMessage extends Message<List<VotePayload>> {
    public VotesListMessage(List<VotePayload> payload) {
        super(MessageType.VOTING_RESULT, payload);
    }
}
