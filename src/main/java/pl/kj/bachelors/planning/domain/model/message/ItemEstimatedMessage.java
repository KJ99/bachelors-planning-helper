package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;
import pl.kj.bachelors.planning.domain.model.payload.PlanningItemPayload;

public class ItemEstimatedMessage extends Message<PlanningItemPayload> {
    public ItemEstimatedMessage(PlanningItemPayload payload) {
        super(MessageType.ITEM_ESTIMATED, payload);
    }
}
