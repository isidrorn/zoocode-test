package io.irn.geoloc.events.search.application;

import io.irn.geoloc.events.application.dto.EventDto;
import io.irn.geoloc.events.geolocation.model.Coordinates;
import io.irn.geoloc.events.search.domain.port.EventQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SearchEventsUseCaseTest {

    @Mock
    private EventQuery eventQuery;

    private SearchEventsUseCase searchEventsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchEventsUseCase = new SearchEventsUseCase(eventQuery);
    }

    @Test
    void searchNearby_shouldReturnEventsWithinRadius() {
        // given
        Coordinates center = new Coordinates(40.7128, -74.0060);
        double radiusKm = 10.0;

        EventDto eventDto = new EventDto(
                java.util.UUID.randomUUID(),
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        when(eventQuery.findNearby(center, radiusKm)).thenReturn(Arrays.asList(eventDto));

        // when
        List<EventDto> result = searchEventsUseCase.searchNearby(center, radiusKm);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Test Event");
        verify(eventQuery, times(1)).findNearby(center, radiusKm);
    }

    @Test
    void searchNearbyByCategory_shouldReturnEventsWithinRadiusAndCategory() {
        // given
        Coordinates center = new Coordinates(40.7128, -74.0060);
        double radiusKm = 10.0;
        String category = "technology";

        EventDto eventDto = new EventDto(
                java.util.UUID.randomUUID(),
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        when(eventQuery.findNearbyByCategory(center, radiusKm, category)).thenReturn(Arrays.asList(eventDto));

        // when
        List<EventDto> result = searchEventsUseCase.searchNearbyByCategory(center, radiusKm, category);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).category()).isEqualTo("technology");
        verify(eventQuery, times(1)).findNearbyByCategory(center, radiusKm, category);
    }

    @Test
    void searchNearbyByDate_shouldReturnEventsWithinRadiusAndDateRange() {
        // given
        Coordinates center = new Coordinates(40.7128, -74.0060);
        double radiusKm = 10.0;
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = OffsetDateTime.now().plusDays(7);

        EventDto eventDto = new EventDto(
                java.util.UUID.randomUUID(),
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        when(eventQuery.findNearbyByDate(center, radiusKm, start, end)).thenReturn(Arrays.asList(eventDto));

        // when
        List<EventDto> result = searchEventsUseCase.searchNearbyByDate(center, radiusKm, start, end);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Test Event");
        verify(eventQuery, times(1)).findNearbyByDate(center, radiusKm, start, end);
    }
}
