package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.message.VotingStatusPayload;

public class VotingStatusMessage extends Message<VotingStatusPayload> {
    public VotingStatusMessage(boolean enabled) {
        super(MessageType.VOTING_STATUS, new VotingStatusPayload(enabled));
    }
}
