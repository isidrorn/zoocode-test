package io.irn.geoloc.events.domain.port;

import io.irn.geoloc.events.domain.model.Event;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

/**
 * Outbound port for persisting and retrieving Event aggregates.
 */
public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(UUID id);
    List<Event> findAll();
    void deleteById(UUID id);
}
