package io.irn.geoloc.events.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.domain.model.*;
import io.irn.geoloc.events.domain.port.EventRepository;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case for creating a new Event.
 */
@Service
public class CreateEventHandler {

    private final EventRepository repository;

    public CreateEventHandler(EventRepository repository) {
        this.repository = repository;
    }

    public EventDto handle(EventDto dto) {
        // Map DTO to domain model
        Category category = dto.category() != null ? new Category(null, dto.category()) : null;
        Schedule schedule = (dto.startAt() != null || dto.endAt() != null) ?
                new Schedule(null, dto.startAt(), dto.endAt()) : null;
        Organizer organizer = dto.organizer() != null ? new Organizer(null, dto.organizer(), null) : null;
        EventStatus status = dto.status() != null ? EventStatus.valueOf(dto.status()) : EventStatus.DRAFT;
        Event event = new Event(
                dto.id() != null ? dto.id() : UUID.randomUUID(),
                dto.title(),
                dto.description(),
                category,
                schedule,
                organizer,
                status,
                dto.source(),
                dto.sourceId(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        Event saved = repository.save(event);
        return toDto(saved);
    }

    private EventDto toDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory() != null ? event.getCategory().getName() : null,
                event.getOrganizer() != null ? event.getOrganizer().getName() : null,
                event.getSchedule() != null ? event.getSchedule().getStartAt() : null,
                event.getSchedule() != null ? event.getSchedule().getEndAt() : null,
                event.getStatus() != null ? event.getStatus().name() : null,
                event.getSource(),
                event.getSourceId()
        );
    }
}
