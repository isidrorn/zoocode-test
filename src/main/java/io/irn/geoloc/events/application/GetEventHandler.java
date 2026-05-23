package io.irn.geoloc.events.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.domain.model.Event;
import io.irn.geoloc.events.domain.port.EventRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for retrieving an Event by its ID and listing all events.
 */
@Service
public class GetEventHandler {

    private final EventRepository repository;

    public GetEventHandler(EventRepository repository) {
        this.repository = repository;
    }

    public Optional<EventDto> handle(UUID id) {
        Optional<Event> eventOpt = repository.findById(id);
        return eventOpt.map(this::toDto);
    }

    public List<EventDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
