package pl.kj.bachelors.planning.domain.model.event;

import pl.kj.bachelors.planning.domain.model.payload.EstimationCommandPayload;

public class EstimateCommandReceivedEvent extends CommandReceivedEvent<EstimationCommandPayload> {
    public EstimateCommandReceivedEvent(Object source, EstimationCommandPayload payload) {
        super(source, payload);
    }
}
