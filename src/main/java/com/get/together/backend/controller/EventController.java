package com.get.together.backend.controller;

import com.get.together.backend.controller.model.Event;
import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.service.EventService;
import com.get.together.backend.data.service.UserService;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.CryptographyUtil;
import com.get.together.backend.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

import static com.get.together.backend.controller.util.Parsers.*;

@Slf4j
@RestController
public class EventController {
    final EventService eventService;
    final UserService userService;
    final CryptographyUtil cryptographyUtil;

    @Autowired
    public EventController(EventService eventService, UserService userService, CryptographyUtil cryptographyUtil) {
        this.eventService = eventService;
        this.userService = userService;
        this.cryptographyUtil = cryptographyUtil;
    }

    @RequestMapping(value = "/v1/event/{id}", method = RequestMethod.GET)
    private ResponseEntity<Event> getEventByIdV1(@PathVariable String id) {
        log.info("Calling: getEventByIdV1 >> ".concat(id));

        val response = eventService.findById(tryParseInteger(id, "id"));

        return ResponseEntity.ok(mapEvent(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_header_and_active_and_created_before_and_after/" +
            "{header}/{active}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> getEventLikeHeaderAndActiveAndCreatedBeforeAndAfterV1
            (@PathVariable String header,
             @PathVariable String active,
             @RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getEventLikeHeaderAndActiveAndCreatedBeforeAndAfterV1 >> "
                .concat("header: ").concat(header).concat(" active: ".concat(active))
                .concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                (header, tryParseBoolean(active, "active"),
                        new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_header_and_created_before_and_after/{header}",
            method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> getEventLikeHeaderAndCreatedBeforeAndAfterV1
            (@PathVariable String header,
             @RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getEventLikeHeaderAndCreatedBeforeAndAfterV1 >> "
                .concat("header: ").concat(header).concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                (header, new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_description_and_created_before_and_after/{description}",
            method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
            (@PathVariable String description,
             @RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getEventLikeHeaderAndCreatedBeforeAndAfterV1 >> "
                .concat("description: ").concat(description).concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                (description, new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_active_and_created_before_and_after/{active}",
            method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> findAllByIsActiveAndCreatedBeforeAndCreatedAfter
            (@PathVariable String active,
             @RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getEventLikeHeaderAndCreatedBeforeAndAfterV1 >> "
                .concat("active: ").concat(active).concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByIsActiveAndCreatedBeforeAndCreatedAfter
                (tryParseBoolean(active, "active"),
                        new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_between_created_before_and_after", method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> findAllBetweenCreatedBeforeAndCreatedAfterV1
            (@RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: findAllBetweenCreatedBeforeAndCreatedAfterV1 >> "
                .concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByCreatedBeforeAndCreatedAfter
                (new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_by_capacity_between/{min}/{max}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> findAllByCapacityBetweenV1
            (@PathVariable String min,
             @PathVariable String max,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: findAllByCapacityBetweenV1 >> "
                .concat(" min: ").concat(min)
                .concat(" | ".concat("max: ".concat(max))));

        val response = eventService.findAllByCapacityBetween
                (tryParseInteger(min, "min"), tryParseInteger(max, "max"),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_by_capacity_between_and_attending_between/" +
            "{capacityMin}/{capacityMax}/{attendingMin}/{attendingMax}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> findAllByCapacityBetweenAndAttendingBetweenV1
            (@PathVariable String capacityMin,
             @PathVariable String capacityMax,
             @PathVariable String attendingMin,
             @PathVariable String attendingMax,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: findAllByCapacityBetweenV1 >> "
                .concat(" capacityMin: ").concat(capacityMin)
                .concat(" | ".concat("capacityMax: ".concat(capacityMax))
                        .concat(" attendingMin: ").concat(attendingMin)
                        .concat(" | ").concat("attendingMax").concat(attendingMax)));

        val response = eventService.findAllByCapacityBetweenAndAttendingBetween
                (tryParseInteger(capacityMin, "capacityMax"),
                        tryParseInteger(capacityMax, "capacityMax"),
                        tryParseInteger(attendingMin, "attendingMin"),
                        tryParseInteger(attendingMax, "attendingMax"),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_description_and_active_and_created_before_and_after/" +
            "{description}/{active}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<Event>> getEventLikeDescriptionAndActiveAndCreatedBeforeAndAfterV1
            (@PathVariable String description,
             @PathVariable String active,
             @RequestParam(defaultValue = "") String createdBefore,
             @RequestParam(defaultValue = "") String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getEventLikeDescriptionAndActiveAndCreatedBeforeAndAfterV1 >> "
                .concat("description: ").concat(description).concat(" active: ".concat(active))
                .concat(" createdBefore: ").concat(createdBefore)
                .concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                (description, tryParseBoolean(active, "active"),
                        new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")),
                        pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/save", method = RequestMethod.POST)
    private ResponseEntity<Event> saveEventV1(@RequestBody Event event) {
        log.info("Calling: saveEventV1 >> ".concat(event.toString()));

        val host = userService.findById(event.getHostId());

        val response = eventService.save(EventModel.builder()
                .id(event.getId())
                .host(host)
                .attending(event.getAttending())
                .capacity(event.getCapacity())
                .description(event.getDescription())
                .header(event.getHeader())
                .isActive(Objects.isNull(event.getIsActive()) || event.getIsActive())
                .build());

        return ResponseEntity.ok(mapEvent(response));
    }

    @RequestMapping(value = "/v1/event/delete/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<Event> deleteEventV1(@PathVariable String id) {
        log.info("Calling: deleteEventV1 >> id: ".concat(id));

        val response = eventService.hardDelete(tryParseInteger(id, "id"));

        return ResponseEntity.ok(mapEvent(response));
    }

    private PagedData<Event> mapPagedData(GenericPagedModel<EventModel> model) {
        return PagedData.<Event>builder()
                .totalElements(model.getTotalElements())
                .numberOfElements(model.getNumberOfElements())
                .totalPages(model.getTotalPages())
                .content(mapEvents(model.getContent()))
                .build();
    }

    private Collection<Event> mapEvents(Collection<EventModel> models) {
        return new ArrayList<>(models.stream().map(this::mapEvent).toList());
    }

    private Event mapEvent(EventModel model) {
        return Event.builder()
                .id(model.getId())
                .attending(model.getAttending())
                .hostId(model.getHost().getId())
                .capacity(model.getCapacity())
                .description(model.getDescription())
                .header(model.getHeader())
                .isActive(model.getIsActive())
                .created(model.getCreated())
                .build();
    }
}
