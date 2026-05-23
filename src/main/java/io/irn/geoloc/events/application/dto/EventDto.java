package io.irn.geoloc.events.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Event API.
 */
public record EventDto(
        UUID id,
        String title,
        String description,
        String category,
        String organizer,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String status,
        String source,
        String sourceId
) {}
