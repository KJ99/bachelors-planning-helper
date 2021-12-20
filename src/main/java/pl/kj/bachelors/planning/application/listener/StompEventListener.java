package pl.kj.bachelors.planning.application.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;
import pl.kj.bachelors.planning.domain.model.message.MemberJoinMessage;
import pl.kj.bachelors.planning.domain.model.message.MemberLeftMessage;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

@Component
public class StompEventListener {
    private final SimpMessagingTemplate messenger;

    @Autowired
    public StompEventListener(SimpMessagingTemplate messenger) {
        this.messenger = messenger;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        String uid = RequestHandler.getCurrentUserId().orElse(null);
        Integer planningId = RequestHandler.getAttribute(RequestAttributeName.PLANNING_ID.value, Integer.class)
                .orElseThrow();
        String messageDestination = String.format("/members/%s", planningId);
        MemberJoinMessage message = new MemberJoinMessage(uid);

        this.messenger.convertAndSend(messageDestination, message);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String uid = RequestHandler.getCurrentUserId().orElse(null);
        Integer planningId = RequestHandler.getAttribute(RequestAttributeName.PLANNING_ID.value, Integer.class)
                .orElseThrow();
        String messageDestination = String.format("/members/%s", planningId);
        MemberLeftMessage message = new MemberLeftMessage(uid);

        this.messenger.convertAndSend(messageDestination, message);
    }

}
