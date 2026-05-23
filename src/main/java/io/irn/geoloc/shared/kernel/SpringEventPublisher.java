package io.irn.geoloc.shared.kernel;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring implementation of the {@link EventPublisher} port that delegates to Spring's {@link ApplicationEventPublisher}.
 */
@Component
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher springPublisher;

    public SpringEventPublisher(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        // Spring's ApplicationEventPublisher works with any object, so we can publish the domain event directly.
        springPublisher.publishEvent(event);
    }
}
