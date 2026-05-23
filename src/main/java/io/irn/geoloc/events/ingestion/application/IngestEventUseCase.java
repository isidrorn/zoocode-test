package io.irn.geoloc.events.ingestion.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.domain.model.*;
import io.irn.geoloc.events.domain.port.EventRepository;
import io.irn.geoloc.events.ingestion.domain.EventCreated;
import io.irn.geoloc.events.ingestion.model.RawEvent;
import io.irn.geoloc.shared.kernel.EventPublisher;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Use case for ingesting a raw event from external sources.
 */
@Service
public class IngestEventUseCase {

    private final EventRepository repository;
    private final EventPublisher eventPublisher;

    public IngestEventUseCase(EventRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Ingests the raw event, persists the Event aggregate and publishes an {@link EventCreated} domain event.
     *
     * @param raw the raw event payload
     * @return the created {@link EventDto}
     */
    public EventDto ingest(RawEvent raw) {
        // Build domain model
        Category category = raw.category() != null ? new Category(null, raw.category()) : null;
        Schedule schedule = (raw.startAt() != null || raw.endAt() != null) ?
                new Schedule(null, raw.startAt(), raw.endAt()) : null;
        Organizer organizer = raw.organizer() != null ? new Organizer(null, raw.organizer(), null) : null;
        Event event = new Event(
                UUID.randomUUID(),
                raw.title(),
                raw.description(),
                category,
                schedule,
                organizer,
                EventStatus.DRAFT,
                raw.source(),
                raw.sourceId(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        // Persist
        Event saved = repository.save(event);
        // Publish domain event with coordinates if present
        eventPublisher.publish(new EventCreated(
                saved.getId(),
                saved.getSource(),
                saved.getSourceId(),
                raw.latitude(),
                raw.longitude()
        ));
        // Return DTO
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
