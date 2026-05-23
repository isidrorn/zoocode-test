package io.irn.geoloc.events.adapter.web;

import io.irn.geoloc.events.application.CreateEventHandler;
import io.irn.geoloc.events.application.DeleteEventHandler;
import io.irn.geoloc.events.application.GetEventHandler;
import io.irn.geoloc.events.application.UpdateEventHandler;
import io.irn.geoloc.events.application.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for Event CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final CreateEventHandler createHandler;
    private final GetEventHandler getHandler;
    private final UpdateEventHandler updateHandler;
    private final DeleteEventHandler deleteHandler;

    public EventController(CreateEventHandler createHandler,
                           GetEventHandler getHandler,
                           UpdateEventHandler updateHandler,
                           DeleteEventHandler deleteHandler) {
        this.createHandler = createHandler;
        this.getHandler = getHandler;
        this.updateHandler = updateHandler;
        this.deleteHandler = deleteHandler;
    }

    @PostMapping
    public ResponseEntity<EventDto> create(@RequestBody EventDto dto) {
        EventDto created = createHandler.handle(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> get(@PathVariable UUID id) {
        Optional<EventDto> dto = getHandler.handle(id);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> list() {
        List<EventDto> all = getHandler.getAll();
        return ResponseEntity.ok(all);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(@PathVariable UUID id, @RequestBody EventDto dto) {
        Optional<EventDto> updated = updateHandler.handle(id, dto);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
