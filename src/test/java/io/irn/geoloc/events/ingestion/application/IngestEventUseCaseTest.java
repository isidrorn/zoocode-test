package io.irn.geoloc.events.ingestion.application;

import io.irn.geoloc.events.ingestion.model.RawEvent;
import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.domain.model.*;
import io.irn.geoloc.events.domain.port.EventRepository;
import io.irn.geoloc.shared.kernel.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IngestEventUseCaseTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventPublisher eventPublisher;

    private IngestEventUseCase ingestEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingestEventUseCase = new IngestEventUseCase(eventRepository, eventPublisher);
    }

    @Test
    void ingest_shouldPersistEventAndPublishEventCreated_whenRawEventHasCoordinates() {
        // given
        RawEvent rawEvent = new RawEvent(
                "test-source",
                "test-123",
                "Test Event",
                "A description",
                "technology",
                "Test Organizer",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                40.7128,
                -74.0060
        );

        Event event = new Event(
                UUID.randomUUID(),
                "Test Event",
                "A description",
                new Category(null, "technology"),
                new Schedule(null, OffsetDateTime.now(), OffsetDateTime.now().plusHours(2)),
                new Organizer(null, "Test Organizer", null),
                EventStatus.DRAFT,
                "test-source",
                "test-123",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventRepository.findById(any(UUID.class))).thenReturn(Optional.of(event));

        // when
        EventDto result = ingestEventUseCase.ingest(rawEvent);

        // then
        assertThat(result.title()).isEqualTo("Test Event");
        assertThat(result.source()).isEqualTo("test-source");
        assertThat(result.status()).isEqualTo("DRAFT");

        // verify that a domain event was published
        verify(eventPublisher, times(1)).publish(any(io.irn.geoloc.events.ingestion.domain.EventCreated.class));
    }

    @Test
    void ingest_shouldPersistEventAndPublishEventCreated_whenRawEventLacksCoordinates() {
        // given
        RawEvent rawEvent = new RawEvent(
                "test-source",
                "test-456",
                "Another Event",
                "Another description",
                "social",
                "Community Group",
                OffsetDateTime.now().plusDays(7),
                null,
                null,
                null
        );

        Event event = new Event(
                UUID.randomUUID(),
                "Another Event",
                "Another description",
                null,
                null,
                new Organizer(null, "Community Group", null),
                EventStatus.DRAFT,
                "test-source",
                "test-456",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventRepository.findById(any(UUID.class))).thenReturn(Optional.of(event));

        // when
        EventDto result = ingestEventUseCase.ingest(rawEvent);

        // then
        assertThat(result.title()).isEqualTo("Another Event");
        assertThat(result.source()).isEqualTo("test-source");
        assertThat(result.status()).isEqualTo("DRAFT");

        // verify that a domain event was published
        verify(eventPublisher, times(1)).publish(any(io.irn.geoloc.events.ingestion.domain.EventCreated.class));
    }
}
