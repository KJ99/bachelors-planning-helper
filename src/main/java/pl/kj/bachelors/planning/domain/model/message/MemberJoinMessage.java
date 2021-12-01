package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.message.MemberPayload;

public class MemberJoinMessage extends Message<MemberPayload> {
    public MemberJoinMessage(String userId) {
        super(MessageType.MEMBER_JOIN, new MemberPayload(userId));
    }
}
