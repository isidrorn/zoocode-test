package io.irn.geoloc.shared.kernel;

/**
 * Port for publishing domain events.
 */
@FunctionalInterface
public interface EventPublisher {
    /**
     * Publish a domain event.
     */
    void publish(DomainEvent event);
}
