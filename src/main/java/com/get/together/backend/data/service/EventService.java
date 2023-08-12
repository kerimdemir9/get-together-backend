package com.get.together.backend.data.service;

import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.repository.EventRepository;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.data.validator.EventValidator;
import com.get.together.backend.util.SortDirection;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;

@Service
public class EventService {

    final EventRepository eventRepository;
    final EventValidator eventValidator;

    @Autowired
    public EventService(EventRepository eventRepository, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    public EventModel findById(Integer id) {
        try {
            if (Objects.isNull(id)) {
                throw new ResponseStatusException
                        (HttpStatus.NOT_ACCEPTABLE, "eventId must not be null");
            }
            val result = eventRepository.findById(id);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "No event with id: ".concat(id.toString()));
            }
            return result;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException
                    (HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<EventModel> findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
            (String header, Boolean isActive,
             Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                    (header, isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                    (header, isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event with header: ".concat(header)
                        .concat(" active: ".concat(isActive.toString())
                                .concat(" createdBefore: ".concat(createdBefore.toString())
                                        .concat(" | createdAfter: ".concat(createdAfter.toString())))));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
            (String description, Boolean isActive,
             Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                    (description, isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
                    (description, isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event with description: ".concat(description)
                        .concat(" active: ".concat(isActive.toString())
                                .concat(" createdBefore: ".concat(createdBefore.toString())
                                        .concat(" | createdAfter: ".concat(createdAfter.toString())))));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
            (String header, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                    (header, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                    (header, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event with header: ".concat(header)
                        .concat(" createdBefore: ".concat(createdBefore.toString())
                                .concat(" | createdAfter: ".concat(createdAfter.toString()))));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
            (String description, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                    (description, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
                    (description, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event with description: ".concat(description)
                        .concat(" createdBefore: ".concat(createdBefore.toString())
                                .concat(" | createdAfter: ".concat(createdAfter.toString()))));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByIsActiveAndCreatedBeforeAndCreatedAfter
            (Boolean isActive, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByIsActiveAndCreatedBeforeAndCreatedAfter
                    (isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByIsActiveAndCreatedBeforeAndCreatedAfter
                    (isActive, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event with isActive: ".concat(isActive.toString())
                        .concat(" createdBefore: ".concat(createdBefore.toString())
                                .concat(" | createdAfter: ".concat(createdAfter.toString()))));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByCreatedBeforeAndCreatedAfter
            (Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByCreatedBeforeAndCreatedAfter
                    (createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByCreatedBeforeAndCreatedAfter
                    (createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event createdBefore: "
                        .concat(createdBefore.toString())
                        .concat(" | createdAfter: ".concat(createdAfter.toString())));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByCapacityBetween(
            Integer min, Integer max,
            int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByCapacityBetween
                    (min,max, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByCapacityBetween
                    (min, max, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event capacity between: "
                        .concat(min.toString()).concat(" and ").concat(max.toString()));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<EventModel> findAllByCapacityBetween(
            Integer capacityMin, Integer capacityMax,
            Integer attendingMin, Integer attendingMax,
            int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? eventRepository.findAllByCapacityBetweenAndAttendingBetween
                    (capacityMin,capacityMax, attendingMin, attendingMax,
                            PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : eventRepository.findAllByCapacityBetweenAndAttendingBetween
                    (capacityMin,capacityMax, attendingMin, attendingMax,
                            PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event capacity between: "
                        .concat(capacityMin.toString()).concat(" | ").concat(capacityMax.toString())
                        .concat(" and attending between: ".concat(attendingMin.toString())
                                .concat(" | ").concat(attendingMax.toString())));
            }

            return GenericPagedModel.<EventModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public EventModel save(EventModel eventModel) {
        try {
            eventValidator.validate(eventModel);
            return eventRepository.save(eventModel);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException
                    (HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public EventModel hardDelete(Integer id) {
        try {
            val eventToHardDelete = findById(id);

            eventRepository.delete(eventToHardDelete);

            return eventToHardDelete;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void hardDeleteAll() {
        try {
            eventRepository.deleteAll();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
