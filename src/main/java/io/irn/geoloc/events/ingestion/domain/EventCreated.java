package io.irn.geoloc.events.ingestion.domain;

import io.irn.geoloc.shared.kernel.DomainEvent;
import java.util.UUID;

/**
 * Domain event emitted when a new Event aggregate is created via ingestion.
 */
public class EventCreated implements DomainEvent {
    private final UUID eventId;
    private final String source;
    private final String sourceId;
    private final Double latitude;
    private final Double longitude;

    public EventCreated(UUID eventId, String source, String sourceId, Double latitude, Double longitude) {
        this.eventId = eventId;
        this.source = source;
        this.sourceId = sourceId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getAggregateId() {
        return eventId.toString();
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
