package io.irn.geoloc.events.infrastructure.persistence;

import io.irn.geoloc.events.domain.model.Event;
import io.irn.geoloc.events.domain.port.EventRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of the EventRepository port.
 */
@Component
public class JpaEventRepository implements EventRepository {

    private final EventJpaRepository jpaRepository;

    public JpaEventRepository(EventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        EventEntity entity = EventMapper.toEntity(event);
        EventEntity saved = jpaRepository.save(entity);
        return EventMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findById(UUID id) {
        return jpaRepository.findById(id).map(EventMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return jpaRepository.findAll().stream()
                .map(EventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
