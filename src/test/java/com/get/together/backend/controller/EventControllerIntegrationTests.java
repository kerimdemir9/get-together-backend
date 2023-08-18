package com.get.together.backend.controller;

import com.get.together.backend.TestBase;
import com.get.together.backend.controller.model.Event;
import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.model.UserModel;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


public class EventControllerIntegrationTests extends TestBase {

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

    public void testPagedDataResponse(PagedData<Event> model) {
        assertNotNull(model);
        assertFalse(model.getContent().isEmpty());
        assertEquals(Optional.of(2L), Optional.of(model.getTotalElements()));
        assertEquals(Optional.of(1), Optional.of(model.getTotalPages()));
        assertEquals(Optional.of(2), Optional.of(model.getNumberOfElements()));

        assertTrue(model.getContent()
                .stream()
                .anyMatch(f -> String.valueOf(f.getId()).equals(newEvent1.getId().toString())));

        assertTrue(model.getContent()
                .stream()
                .anyMatch(f -> String.valueOf(f.getId()).equals(newEvent2.getId().toString())));
    }

    @Before
    public void setup() {
        eventService.hardDeleteAll();
        userService.hardDeleteAll();
    }

    @Test
    public void find_by_id_test() {
        insertNewUser();
        insertNewEvent1();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        val response = restTemplate.exchange
                (url.concat("/").concat(newEvent1.getId().toString()),
                        HttpMethod.GET, null, Event.class);

        assertNotNull(response.getBody());
        assertEquals(newEvent1.getId(), response.getBody().getId());
    }

    @Test
    public void insert_event_test() {
        insertNewUser();

        val eventToPost = Event.builder()
                .header("New Event")
                .hostId(newUser.getId())
                .isActive(true)
                .description("New Description")
                .capacity(5)
                .attendees(new ArrayList<>())
                .attending(2)
                .build();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        val response = restTemplate.postForEntity(url.concat("/save"), eventToPost, Event.class);

        assertNotNull(response.getBody());
        assertEquals("New Event", response.getBody().getHeader());
        assertEquals("New Description", response.getBody().getDescription());
    }

    @Test
    public void update_event_test() {
        insertNewUser();
        insertNewEvent1();


        val eventToUpdate = Event.builder()
                .id(newEvent1.getId())
                .header("Updated Event")
                .hostId(newUser.getId())
                .attending(newEvent1.getAttending())
                .isActive(newEvent1.getIsActive())
                .description(newEvent1.getDescription())
                .capacity(newEvent1.getCapacity())
                .build();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event/save");

        val response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(eventToUpdate), Event.class);

        assertNotNull(response.getBody());
        assertEquals("Updated Event", response.getBody().getHeader());
    }

    @Test
    public void delete_event_test() {
        insertNewUser();
        insertNewEvent1();

        val eventToDelete = eventService.findById(newEvent1.getId());

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        val response = restTemplate.exchange(url.concat("/delete/").concat(newEvent1.getId().toString()),
                HttpMethod.DELETE, null, Event.class);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getId(), eventToDelete.getId());
    }

    @Test
    public void find_all_like_header_and_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_like_header_and_active_and_created_before_and_after/header/true?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_like_header_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_like_header_and_created_before_and_after/header?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_like_description_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_like_description_and_created_before_and_after/desc?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_active_and_created_before_and_after/true?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_between_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_between_created_before_and_after?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_like_description_and_active_and_created_before_and_after_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val yesterday = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli();
        val tomorrow = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_like_description_and_active_and_created_before_and_after/desc/true?createdBefore=")
                                .concat(String.valueOf(tomorrow)).concat("&createdAfter=").concat(String.valueOf(yesterday)),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_by_capacity_between_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_by_capacity_between/0/10"),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }

    @Test
    public void find_all_by_capacity_between_and_attending_between_test() {
        insertNewUser();
        insertNewEvent1();
        insertNewEvent2();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/event");

        testPagedDataResponse(restTemplate.exchange
                (url.concat("/find_all_by_capacity_between_and_attending_between/0/10/0/10"),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedData<Event>>() {}).getBody());
    }
}
