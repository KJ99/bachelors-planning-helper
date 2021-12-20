package pl.kj.bachelors.planning.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.event.*;
import pl.kj.bachelors.planning.domain.model.extension.CommandType;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;
import pl.kj.bachelors.planning.domain.model.payload.EstimationCommandPayload;
import pl.kj.bachelors.planning.domain.model.payload.PlanningItemCommandPayload;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class WebSocketController {
    private final ObjectMapper objectMapper;
    private final PlanningRepository planningRepository;
    private final EntityAccessControlService<Planning> accessControl;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public WebSocketController(
            ObjectMapper objectMapper,
            PlanningRepository planningRepository,
            EntityAccessControlService<Planning> accessControl,
            ApplicationEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.planningRepository = planningRepository;
        this.accessControl = accessControl;
        this.eventPublisher = eventPublisher;
    }

    @MessageMapping("/command")
    public void commandReceived(Message<String> message)
            throws IOException, AccessDeniedException, ResourceNotFoundException {
        Integer planningId = RequestHandler.getAttribute(RequestAttributeName.PLANNING_ID.value, Integer.class).orElseThrow();
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        JsonNode messageJson = this.objectMapper.readTree(message.getPayload().getBytes(StandardCharsets.UTF_8));
        CommandType type = CommandType.valueOf(messageJson.get("type").textValue());
        this.accessControl.ensureThatUserHasAccess(planning, type);

        ApplicationEvent event = this.createEventForCommand(type, messageJson.get("payload"));
        if(event != null) {
            this.eventPublisher.publishEvent(event);
        }
    }

    @Nullable
    private ApplicationEvent createEventForCommand(CommandType type, JsonNode payloadJson) {
        ApplicationEvent event;
        switch (type) {
            case RUN:
                event = new RunPlanningCommandReceivedEvent(this);
                break;
            case FOCUS:
                event = new FocusCommandReceivedEvent(
                        this,
                        this.objectMapper.convertValue(payloadJson, PlanningItemCommandPayload.class)
                );
                break;
            case FOCUS_NEXT:
                event = new FocusNextCommandReceivedEvent(this);
                break;
            case START_VOTING:
                event = new StartVotingCommandReceivedEvent(this);
                break;
            case VOTE:
                event = new VoteCommandReceivedEvent(
                        this,
                        this.objectMapper.convertValue(payloadJson, EstimationCommandPayload.class)
                );
                break;
            case STOP_VOTING:
                event = new StopVotingCommandReceivedEvent(this);
                break;
            case RESET_VOTES:
                event = new ResetVotesCommandReceivedEvent(this);
                break;
            case ESTIMATE:
                event = new EstimateCommandReceivedEvent(
                        this,
                        this.objectMapper.convertValue(payloadJson, EstimationCommandPayload.class)
                );
                break;
            default:
                event = null;
                break;
        }

        return event;
    }
}
