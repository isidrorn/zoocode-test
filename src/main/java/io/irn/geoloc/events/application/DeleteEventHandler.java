package io.irn.geoloc.events.application;

import io.irn.geoloc.events.domain.port.EventRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Use case for deleting an Event.
 */
@Service
public class DeleteEventHandler {

    private final EventRepository repository;

    public DeleteEventHandler(EventRepository repository) {
        this.repository = repository;
    }

    public void handle(UUID id) {
        repository.deleteById(id);
    }
}
