package com.get.together.backend.controller;

import com.get.together.backend.controller.model.Event;
import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.service.EventService;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.get.together.backend.controller.util.Parsers.*;

@Controller
@Slf4j
public class EventController {
    final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "/v1/event/{id}", method = RequestMethod.GET)
    private ResponseEntity<Event> getEventByIdV1(@PathVariable String id) {
        log.info("Calling: getEventByIdV1 >> ".concat(id));

        val response = eventService.findById(tryParseInteger(id, "id"));

        return ResponseEntity.ok(mapEvent(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_header_and_active_and_created_before_and_after/{header}&{active}", method = RequestMethod.GET)
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
                .concat(" createdBefore: ").concat(createdBefore).concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                (header, tryParseBoolean(active, "active"), new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")), pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/find_all_like_description_and_active_and_created_before_and_after/{description}&{active}", method = RequestMethod.GET)
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
                .concat(" createdBefore: ").concat(createdBefore).concat(" | ".concat("createdAfter: ".concat(createdAfter))));

        val response = eventService.findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                (description, tryParseBoolean(active, "active"), new Date(tryParseLong(createdBefore, "createdBefore")),
                        new Date(tryParseLong(createdAfter, "createdAfter")), pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/event/save", method = RequestMethod.POST)
    private ResponseEntity<Event> saveEventV1(@RequestBody Event event) {
        log.info("Calling: saveEventV1 >> ".concat(event.toString()));
        val response = eventService.save(EventModel.builder()
                .attending(event.getAttending())
                .capacity(event.getCapacity())
                .description(event.getDescription())
                .header(event.getHeader())
                .isActive(event.getIsActive())
                .created(event.getCreated())
                .build());


        return ResponseEntity.ok(mapEvent(response));
    }

    @RequestMapping(value = "/v1/user/update", method = RequestMethod.POST)
    private ResponseEntity<Event> updateEventV1(@RequestBody Event event) {
        log.info("Calling: updateEventV1 >> ".concat(event.toString()));

        val response = eventService.save(EventModel.builder()
                .id(event.getId())
                .attending(event.getAttending())
                .capacity(event.getCapacity())
                .description(event.getDescription())
                .header(event.getHeader())
                .isActive(event.getIsActive())
                .created(event.getCreated())
                .build());

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
                .capacity(model.getCapacity())
                .description(model.getDescription())
                .header(model.getHeader())
                .isActive(model.getIsActive())
                .created(model.getCreated())
                .build();
    }
}
