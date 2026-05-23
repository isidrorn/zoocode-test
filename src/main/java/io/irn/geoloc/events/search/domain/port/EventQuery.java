package io.irn.geoloc.events.search.domain.port;

import io.irn.geoloc.events.geolocation.model.Coordinates;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Inbound port for querying events by various criteria.
 */
public interface EventQuery {
    /**
     * Find events near a specific location within a given radius.
     *
     * @param center the center coordinates for the search
     * @param radiusKm the search radius in kilometers
     * @return list of events within the specified radius
     */
    List<io.irn.geoloc.events.application.dto.EventDto> findNearby(Coordinates center, double radiusKm);

    /**
     * Find events near a specific location within a given radius, filtered by category.
     *
     * @param center the center coordinates for the search
     * @param radiusKm the search radius in kilometers
     * @param category the category to filter by (optional)
     * @return list of events within the specified radius and category
     */
    List<io.irn.geoloc.events.application.dto.EventDto> findNearbyByCategory(Coordinates center, double radiusKm, String category);

    /**
     * Find events near a specific location within a given radius, filtered by date range.
     *
     * @param center the center coordinates for the search
     * @param radiusKm the search radius in kilometers
     * @param start the start date for the search (optional)
     * @param end the end date for the search (optional)
     * @return list of events within the specified radius and date range
     */
    List<io.irn.geoloc.events.application.dto.EventDto> findNearbyByDate(Coordinates center, double radiusKm, OffsetDateTime start, OffsetDateTime end);
}
