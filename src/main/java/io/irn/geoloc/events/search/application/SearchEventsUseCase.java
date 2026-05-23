package io.irn.geoloc.events.search.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.geolocation.model.Coordinates;
import io.irn.geoloc.events.search.domain.port.EventQuery;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SearchEventsUseCase {
    private final EventQuery eventQuery;

    public SearchEventsUseCase(EventQuery eventQuery) {
        this.eventQuery = eventQuery;
    }

    public List<EventDto> searchNearby(Coordinates center, double radiusKm) {
        return eventQuery.findNearby(center, radiusKm);
    }

    public List<EventDto> searchNearbyByCategory(Coordinates center, double radiusKm, String category) {
        return eventQuery.findNearbyByCategory(center, radiusKm, category);
    }

    public List<EventDto> searchNearbyByDate(Coordinates center, double radiusKm, OffsetDateTime start, OffsetDateTime end) {
        return eventQuery.findNearbyByDate(center, radiusKm, start, end);
    }
}
