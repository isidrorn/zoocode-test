package io.irn.geoloc.events.adapter.web;

import io.irn.geoloc.events.application.CreateEventHandler;
import io.irn.geoloc.events.application.DeleteEventHandler;
import io.irn.geoloc.events.application.GetEventHandler;
import io.irn.geoloc.events.application.UpdateEventHandler;
import io.irn.geoloc.events.application.dto.EventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private CreateEventHandler createHandler;

    @Mock
    private GetEventHandler getHandler;

    @Mock
    private UpdateEventHandler updateHandler;

    @Mock
    private DeleteEventHandler deleteHandler;

    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventController = new EventController(createHandler, getHandler, updateHandler, deleteHandler);
    }

    @Test
    void create_shouldReturnCreatedEvent() {
        // given
        EventDto inputDto = new EventDto(
                UUID.randomUUID(),
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                null,
                null,
                "DRAFT",
                "test-source",
                "test-123"
        );

        EventDto outputDto = new EventDto(
                UUID.randomUUID(),
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                null,
                null,
                "DRAFT",
                "test-source",
                "test-123"
        );

        when(createHandler.handle(inputDto)).thenReturn(outputDto);

        // when
        ResponseEntity<EventDto> response = eventController.create(inputDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("Test Event");
        verify(createHandler, times(1)).handle(inputDto);
    }

    @Test
    void get_shouldReturnEvent_whenExists() {
        // given
        UUID eventId = UUID.randomUUID();
        EventDto eventDto = new EventDto(
                eventId,
                "Test Event",
                "Description",
                "technology",
                "Organizer",
                null,
                null,
                "DRAFT",
                "test-source",
                "test-123"
        );

        when(getHandler.handle(eventId)).thenReturn(Optional.of(eventDto));

        // when
        ResponseEntity<EventDto> response = eventController.get(eventId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(eventId);
        verify(getHandler, times(1)).handle(eventId);
    }

    @Test
    void get_shouldReturnNotFound_whenDoesNotExist() {
        // given
        UUID eventId = UUID.randomUUID();
        when(getHandler.handle(eventId)).thenReturn(Optional.empty());

        // when
        ResponseEntity<EventDto> response = eventController.get(eventId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(getHandler, times(1)).handle(eventId);
    }

    @Test
    void list_shouldReturnAllEvents() {
        // given
        EventDto eventDto1 = new EventDto(
                UUID.randomUUID(),
                "Event 1",
                "Description 1",
                "technology",
                "Organizer 1",
                null,
                null,
                "DRAFT",
                "test-source",
                "test-123"
        );

        EventDto eventDto2 = new EventDto(
                UUID.randomUUID(),
                "Event 2",
                "Description 2",
                "social",
                "Organizer 2",
                null,
                null,
                "PUBLISHED",
                "test-source",
                "test-456"
        );

        when(getHandler.getAll()).thenReturn(List.of(eventDto1, eventDto2));

        // when
        ResponseEntity<List<EventDto>> response = eventController.list();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        verify(getHandler, times(1)).getAll();
    }

    @Test
    void update_shouldReturnUpdatedEvent_whenExists() {
        // given
        UUID eventId = UUID.randomUUID();
        EventDto inputDto = new EventDto(
                eventId,
                "Updated Event",
                "Updated Description",
                "technology",
                "Updated Organizer",
                null,
                null,
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        EventDto outputDto = new EventDto(
                eventId,
                "Updated Event",
                "Updated Description",
                "technology",
                "Updated Organizer",
                null,
                null,
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        when(updateHandler.handle(eventId, inputDto)).thenReturn(Optional.of(outputDto));

        // when
        ResponseEntity<EventDto> response = eventController.update(eventId, inputDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("Updated Event");
        verify(updateHandler, times(1)).handle(eventId, inputDto);
    }

    @Test
    void update_shouldReturnNotFound_whenDoesNotExist() {
        // given
        UUID eventId = UUID.randomUUID();
        EventDto inputDto = new EventDto(
                eventId,
                "Updated Event",
                "Updated Description",
                "technology",
                "Updated Organizer",
                null,
                null,
                "PUBLISHED",
                "test-source",
                "test-123"
        );

        when(updateHandler.handle(eventId, inputDto)).thenReturn(Optional.empty());

        // when
        ResponseEntity<EventDto> response = eventController.update(eventId, inputDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(updateHandler, times(1)).handle(eventId, inputDto);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // given
        UUID eventId = UUID.randomUUID();

        // when
        ResponseEntity<Void> response = eventController.delete(eventId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(deleteHandler, times(1)).handle(eventId);
    }
}
