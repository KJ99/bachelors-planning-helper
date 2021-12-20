package pl.kj.bachelors.planning.domain.model.message;

import pl.kj.bachelors.planning.domain.model.extension.MessageType;

public class PlanningCompletedMessage extends Message<Object> {
    public PlanningCompletedMessage() {
        super(MessageType.PLANNING_COMPLETED);
    }
}
