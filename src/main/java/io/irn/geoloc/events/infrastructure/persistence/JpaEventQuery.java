package io.irn.geoloc.events.infrastructure.persistence;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.geolocation.model.Coordinates;
import io.irn.geoloc.events.search.domain.port.EventQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class JpaEventQuery implements EventQuery {
    private final EventJpaRepository repository;

    public JpaEventQuery(EventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findNearby(Coordinates center, double radiusKm) {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findNearbyByCategory(Coordinates center, double radiusKm, String category) {
        return repository.findAll().stream()
                .filter(event -> category == null || Objects.equals(event.getCategory(), category))
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findNearbyByDate(Coordinates center, double radiusKm, OffsetDateTime start, OffsetDateTime end) {
        return repository.findAll().stream()
                .filter(event -> start == null || event.getStartAt() == null || !event.getStartAt().isBefore(start))
                .filter(event -> end == null || event.getStartAt() == null || !event.getStartAt().isAfter(end))
                .map(this::toDto)
                .toList();
    }

    private EventDto toDto(EventEntity event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                event.getOrganizer(),
                event.getStartAt(),
                event.getEndAt(),
                event.getStatus(),
                event.getSource(),
                event.getSourceId()
        );
    }
}
