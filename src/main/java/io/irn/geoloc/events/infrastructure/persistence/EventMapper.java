package io.irn.geoloc.events.infrastructure.persistence;

import io.irn.geoloc.events.domain.model.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Mapper between domain model {@link Event} and JPA {@link EventEntity}.
 */
public class EventMapper {

    public static EventEntity toEntity(Event event) {
        EventEntity entity = new EventEntity();
        entity.setId(event.getId());
        entity.setTitle(event.getTitle());
        entity.setDescription(event.getDescription());
        entity.setCategory(event.getCategory() != null ? event.getCategory().getName() : null);
        entity.setStatus(event.getStatus() != null ? event.getStatus().name() : null);
        entity.setSource(event.getSource());
        entity.setSourceId(event.getSourceId());
        entity.setOrganizer(event.getOrganizer() != null ? event.getOrganizer().getName() : null);
        if (event.getSchedule() != null) {
            entity.setStartAt(event.getSchedule().getStartAt());
            entity.setEndAt(event.getSchedule().getEndAt());
        }
        entity.setCreatedAt(event.getCreatedAt());
        entity.setUpdatedAt(event.getUpdatedAt());
        return entity;
    }

    public static Event toDomain(EventEntity entity) {
        Category category = entity.getCategory() != null ? new Category(null, entity.getCategory()) : null;
        Schedule schedule = null;
        if (entity.getStartAt() != null) {
            schedule = new Schedule(null, entity.getStartAt(), entity.getEndAt());
        }
        Organizer organizer = entity.getOrganizer() != null ? new Organizer(null, entity.getOrganizer(), null) : null;
        EventStatus status = entity.getStatus() != null ? EventStatus.valueOf(entity.getStatus()) : null;
        return new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                category,
                schedule,
                organizer,
                status,
                entity.getSource(),
                entity.getSourceId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
