package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.MemberPayload;

public class MemberVotedMessage extends Message<MemberPayload> {
    public MemberVotedMessage(String userId) {
        super(MessageType.MEMBER_VOTED, new MemberPayload(userId));
    }
}
