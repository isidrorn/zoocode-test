package io.irn.geoloc.events.domain.model;

import io.irn.geoloc.shared.kernel.ValueObject;
import java.time.OffsetDateTime;

/**
 * Value object representing the schedule of an event.
 */
public class Schedule implements ValueObject {
    private final String id;
    private final OffsetDateTime startAt;
    private final OffsetDateTime endAt;

    public Schedule(String id, OffsetDateTime startAt, OffsetDateTime endAt) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @Override
    public String getId() {
        return id;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }
}
