package io.irn.geoloc.events.ingestion.adapter.web;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.ingestion.application.IngestEventUseCase;
import io.irn.geoloc.events.ingestion.model.RawEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for ingesting raw events from external sources.
 */
@RestController
@RequestMapping("/api/v1/ingest")
public class IngestionController {

    private final IngestEventUseCase ingestEventUseCase;

    public IngestionController(IngestEventUseCase ingestEventUseCase) {
        this.ingestEventUseCase = ingestEventUseCase;
    }

    @PostMapping
    public ResponseEntity<EventDto> ingest(@RequestBody RawEvent rawEvent) {
        EventDto created = ingestEventUseCase.ingest(rawEvent);
        return ResponseEntity.accepted().body(created);
    }
}
