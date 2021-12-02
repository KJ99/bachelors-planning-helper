package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.PlanningItemPayload;

public class ItemFocusedMessage extends Message<PlanningItemPayload> {
    public ItemFocusedMessage(PlanningItemPayload payload) {
        super(MessageType.ITEM_FOCUSED, payload);
    }
}
