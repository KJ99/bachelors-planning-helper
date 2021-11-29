package pl.kj.bachelors.planning.infrastructure.service.management;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public abstract class BaseManagementService {
    private final ApplicationEventPublisher eventPublisher;

    protected BaseManagementService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected void publishEvent(ApplicationEvent event) {
        this.eventPublisher.publishEvent(event);
    }
}
