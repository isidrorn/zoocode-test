package io.irn.geoloc.shared.kernel;

/**
 * Marker interface for domain events.
 */
public interface DomainEvent {
    /**
     * Get the aggregate ID associated with this event.
     */
    String getAggregateId();
}
