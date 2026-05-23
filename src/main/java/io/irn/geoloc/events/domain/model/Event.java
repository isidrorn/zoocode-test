package io.irn.geoloc.events.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Aggregate root representing an Event.
 */
public class Event {
    private final UUID id;
    private final String title;
    private final String description;
    private final Category category;
    private final Schedule schedule;
    private final Organizer organizer;
    private final EventStatus status;
    private final String source;
    private final String sourceId;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public Event(UUID id,
                 String title,
                 String description,
                 Category category,
                 Schedule schedule,
                 Organizer organizer,
                 EventStatus status,
                 String source,
                 String sourceId,
                 OffsetDateTime createdAt,
                 OffsetDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.schedule = schedule;
        this.organizer = organizer;
        this.status = status;
        this.source = source;
        this.sourceId = sourceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public EventStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
