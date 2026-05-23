package io.irn.geoloc.events.ingestion.model;

import java.time.OffsetDateTime;

/**
 * Value object representing a raw event received from external sources.
 */
public record RawEvent(
        String source,
        String sourceId,
        String title,
        String description,
        String category,
        String organizer,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        Double latitude,
        Double longitude
) {}
