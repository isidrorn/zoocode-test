package io.irn.geoloc.events.infrastructure.persistence;

import io.irn.geoloc.events.geolocation.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for spatial queries on EventEntity.
 * Uses PostGIS geography type for location-based searches.
 */
@Repository
public interface EventSearchRepository extends JpaRepository<EventEntity, UUID> {

    /**
     * Find events within a given radius of a specific coordinate.
     * Uses PostGIS ST_DWithin for efficient spatial queries.
     *
     * @param center the center point for the search
     * @param radiusKm the radius in kilometers
     * @return list of events within the specified radius
     */
    @Query(value = "SELECT * FROM events WHERE ST_DWithin(coordinates, :center::geography, :radius)", nativeQuery = true)
    List<EventEntity> findByCoordinatesWithinRadius(
            @Param("center") Coordinates center,
            @Param("radius") double radius
    );
}
