package pl.kj.bachelors.planning.domain.model.event;

import pl.kj.bachelors.planning.domain.model.payload.command.PlanningItemCommandPayload;

public class FocusCommandReceivedEvent extends CommandReceivedEvent<PlanningItemCommandPayload>{
    public FocusCommandReceivedEvent(Object source, PlanningItemCommandPayload payload) {
        super(source, payload);
    }
}
