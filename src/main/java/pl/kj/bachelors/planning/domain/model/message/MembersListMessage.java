package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.MemberPayload;

import java.util.List;

public class MembersListMessage extends Message<List<MemberPayload>> {
    public MembersListMessage(MessageType type, List<MemberPayload> payload) {
        super(type, payload);
    }
}
