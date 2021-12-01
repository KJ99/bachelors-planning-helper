package pl.kj.bachelors.planning.application.listener;

import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import pl.kj.bachelors.planning.domain.model.extension.RequestAttributeName;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.Optional;

@Component
public class StompEventListener {
    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
    }

}
