package io.irn.geoloc.events.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Spring Data JPA repository for EventEntity.
 */
public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {
    // Additional query methods can be defined here if needed.
}
