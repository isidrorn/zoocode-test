package io.irn.geoloc.events.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.domain.model.*;
import io.irn.geoloc.events.domain.port.EventRepository;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for updating an existing Event.
 */
@Service
public class UpdateEventHandler {

    private final EventRepository repository;

    public UpdateEventHandler(EventRepository repository) {
        this.repository = repository;
    }

    public Optional<EventDto> handle(UUID id, EventDto dto) {
        Optional<Event> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }
        Event existing = existingOpt.get();
        // Build updated domain object (keeping immutable pattern)
        Category category = dto.category() != null ? new Category(null, dto.category()) : existing.getCategory();
        Schedule schedule = (dto.startAt() != null || dto.endAt() != null) ?
                new Schedule(null, dto.startAt(), dto.endAt()) : existing.getSchedule();
        Organizer organizer = dto.organizer() != null ? new Organizer(null, dto.organizer(), null) : existing.getOrganizer();
        EventStatus status = dto.status() != null ? EventStatus.valueOf(dto.status()) : existing.getStatus();
        Event updated = new Event(
                existing.getId(),
                dto.title() != null ? dto.title() : existing.getTitle(),
                dto.description() != null ? dto.description() : existing.getDescription(),
                category,
                schedule,
                organizer,
                status,
                dto.source() != null ? dto.source() : existing.getSource(),
                dto.sourceId() != null ? dto.sourceId() : existing.getSourceId(),
                existing.getCreatedAt(),
                OffsetDateTime.now()
        );
        Event saved = repository.save(updated);
        return Optional.of(toDto(saved));
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
