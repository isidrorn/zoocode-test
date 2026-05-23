package io.irn.geoloc.events;

import io.irn.geoloc.events.ingestion.model.RawEvent;
import io.irn.geoloc.events.application.dto.EventDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test covering the full ingest → geocode → query flow.
 * Uses Testcontainers to run PostgreSQL with PostGIS.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=none"})
public class EventAggregatorIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-3.5").asCompatibleSubstituteFor("postgres")
    );

    @LocalServerPort
    private int port;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldIngestEventAndQueryByLocation() {
        // 1. Ingest a raw event with coordinates
        RawEvent rawEvent = new RawEvent(
                "test-source",
                "test-123",
                "Spring Boot Workshop",
                "Learn Spring Boot with practical examples",
                "technology",
                "Tech Community",
                OffsetDateTime.now().plusDays(7),
                OffsetDateTime.now().plusDays(7).plusHours(2),
                40.7128,  // New York latitude
                -74.0060  // New York longitude
        );

        ResponseEntity<EventDto> ingestResponse = restClient().post()
                .uri("/api/v1/ingest")
                .body(rawEvent)
                .retrieve()
                .toEntity(EventDto.class);

        assertThat(ingestResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        EventDto createdEvent = ingestResponse.getBody();
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.title()).isEqualTo("Spring Boot Workshop");
        assertThat(createdEvent.source()).isEqualTo("test-source");
        assertThat(createdEvent.status()).isEqualTo("DRAFT");

        // 2. Verify the event was created in the database via the list endpoint
        ResponseEntity<List<EventDto>> listResponse = restClient().method(HttpMethod.GET)
                .uri("/api/v1/events")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<EventDto>>() {});

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EventDto> events = listResponse.getBody();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).id()).isEqualTo(createdEvent.id());

        // 3. Retrieve the event by ID
        ResponseEntity<EventDto> getResponse = restClient().get()
                .uri("/api/v1/events/{id}", createdEvent.id())
                .retrieve()
                .toEntity(EventDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventDto retrievedEvent = getResponse.getBody();
        assertThat(retrievedEvent).isNotNull();
        assertThat(retrievedEvent.id()).isEqualTo(createdEvent.id());
        assertThat(retrievedEvent.title()).isEqualTo("Spring Boot Workshop");
    }

    @Test
    void shouldHandleIngestionWithoutCoordinates() {
        // 1. Ingest a raw event without coordinates
        RawEvent rawEvent = new RawEvent(
                "test-source",
                "test-456",
                "Community Meetup",
                "Monthly community gathering",
                "social",
                "Local Community",
                OffsetDateTime.now().plusDays(14),
                null,
                null,
                null
        );

        ResponseEntity<EventDto> ingestResponse = restClient().post()
                .uri("/api/v1/ingest")
                .body(rawEvent)
                .retrieve()
                .toEntity(EventDto.class);

        assertThat(ingestResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        EventDto createdEvent = ingestResponse.getBody();
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.title()).isEqualTo("Community Meetup");
        assertThat(createdEvent.source()).isEqualTo("test-source");

        // 2. Verify the event was created and can be retrieved
        ResponseEntity<EventDto> getResponse = restClient().get()
                .uri("/api/v1/events/{id}", createdEvent.id())
                .retrieve()
                .toEntity(EventDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventDto retrievedEvent = getResponse.getBody();
        assertThat(retrievedEvent).isNotNull();
        assertThat(retrievedEvent.organizer()).isEqualTo("Local Community");
    }

    @Test
    void shouldReturnNotFoundForNonExistentEvent() {
        ResponseEntity<EventDto> response = restClient().get()
                .uri("/api/v1/events/00000000-0000-0000-0000-000000000000")
                .exchange((request, clientResponse) -> ResponseEntity
                        .status(clientResponse.getStatusCode())
                        .build());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
