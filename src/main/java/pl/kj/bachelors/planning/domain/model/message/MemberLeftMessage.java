package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.message.MemberPayload;

public class MemberLeftMessage extends Message<MemberPayload> {
    public MemberLeftMessage(String userId) {
        super(MessageType.MEMBER_LEFT, new MemberPayload(userId));
    }
}
