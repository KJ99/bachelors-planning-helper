package pl.kj.bachelors.planning.domain.model.event;

import pl.kj.bachelors.planning.domain.model.payload.EstimationCommandPayload;

public class VoteCommandReceivedEvent extends CommandReceivedEvent<EstimationCommandPayload> {
    public VoteCommandReceivedEvent(Object source, EstimationCommandPayload payload) {
        super(source, payload);
    }
}
