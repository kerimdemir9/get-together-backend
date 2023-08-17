package com.get.together.backend.data;

import com.get.together.backend.TestBase;
import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class EventServiceIntegrationTests extends TestBase {
    private static EventModel newEvent1;
    private static EventModel newEvent2;

    private static UserModel newUser;

    public void insertNewUser() {
        newUser = userService.save(UserModel.builder()
                .userName("user1")
                .mail("user@gmail.com")
                .firstName("F1")
                .lastName("L1")
                .password("P1")
                .biography("")
                .phoneNumber("12345")
                .created(new Date(Instant.now().toEpochMilli()))
                .build());
    }

    public void insertNewEvent1() {
        newEvent1 = eventService.save(EventModel.builder()
                .host(newUser)
                .header("header1")
                .description("description1")
                .capacity(5)
                .attending(2)
                .isActive(true)
                .attendees(new ArrayList<>())
                .build());
    }

    public void insertNewEvent2() {
        newEvent2 = eventService.save(EventModel.builder()
                .host(newUser)
                .header("header2")
                .description("description2")
                .capacity(10)
                .attending(10)
                .isActive(true)
                .attendees(new ArrayList<>())
                .build());
    }

    public void testCollection(GenericPagedModel<EventModel> users) {
        assertFalse(users.getContent().isEmpty());

        assertTrue(users.getContent()
                .stream()
                .anyMatch(c -> c.getId().equals(newEvent1.getId())));

        assertTrue(users.getContent()
                .stream()
                .anyMatch(c -> c.getId().equals(newEvent2.getId())));
    }

    @Before
    public void setup() {
        eventService.hardDeleteAll();
        userService.hardDeleteAll();
    }

    @Test
    public void find_event_by_id_test() {
        insertNewUser();
        insertNewEvent1();

        val found = eventService.findById(newEvent1.getId());

        assertNotNull(found);
        assertEquals(found.getId(), newEvent1.getId());
    }

    @Test
    public void insert_new_event_test() {
        insertNewUser();
        insertNewEvent1();

        assertNotNull(newEvent1);
        assertEquals("header1", newEvent1.getHeader());
        assertEquals("description1", newEvent1.getDescription());
    }

    @Test
    public void update_event_test() {
        insertNewUser();
        insertNewEvent1();

        newEvent1.setHeader("Updated Event");

        val updatedEvent = eventService.save(newEvent1);

        assertNotNull(updatedEvent);
        assertEquals(newEvent1.getId(), updatedEvent.getId());
        assertEquals("Updated Event", updatedEvent.getHeader());
    }

    @Test
    public void delete_event_test() {
        insertNewUser();
        insertNewEvent1();

        val deleted = eventService.hardDelete(newEvent1.getId());

        assertEquals(deleted.getId(), newEvent1.getId());
    }

    @Test
    public void find_all_like_header_and_is_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                ("header", true, tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_header_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                ("header", tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_description_and_is_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                ("description", true, tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_description_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                ("description", tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_by_is_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByIsActiveAndCreatedBeforeAndCreatedAfter
                (true, tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_by_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val tomorrow = new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        val yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        testCollection(eventService.findAllByCreatedBeforeAndCreatedAfter
                (tomorrow, yesterday, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_by_capacity_between_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        testCollection(eventService.findAllByCapacityBetween
                (5, 10, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_by_capacity_between_and_attending_between_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        testCollection(eventService.findAllByCapacityBetweenAndAttendingBetween
                (5, 10, 2, 10, 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void get_attendees_by_event_id_test() {
        insertNewUser();
        insertNewEvent1();

        val found = eventService.getAttendees(newEvent1.getId());

        assertEquals(found.size(), 0);
    }

    @Test
    public void add_attendee_by_event_id_and_user_id_test() {
        insertNewUser();
        insertNewEvent1();

        val found = eventService.addAttendee
                (newEvent1.getId(), newUser.getId());

        assertNotNull(found);
        assertEquals(found.get(0).getId(), newUser.getId());
        assertEquals(found.get(0).getUserName(), newUser.getUserName());
    }
}
