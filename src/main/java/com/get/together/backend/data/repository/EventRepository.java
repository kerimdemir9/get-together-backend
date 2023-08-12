package com.get.together.backend.data.repository;

import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EventRepository extends PagingAndSortingRepository<EventModel, Integer> {
    EventModel findById(Integer id);

    Page<EventModel> findAllByHeaderContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
            (String header, Boolean isActive, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByHeaderContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
            (String header, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByDescriptionContainingIgnoreCaseAndIsActiveAndCreatedBeforeAndCreatedAfter
            (String description, Boolean isActive, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByDescriptionContainingIgnoreCaseAndCreatedBeforeAndCreatedAfter
            (String description, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByIsActiveAndCreatedBeforeAndCreatedAfter
            (Boolean isActive, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByCreatedBeforeAndCreatedAfter
            (Date createdBefore, Date createdAfter, Pageable pageable);
    Page<EventModel> findAllByCapacityBetween(Integer capacityMin, Integer capacityMax, Pageable pageable);
    Page<EventModel> findAllByCapacityBetweenAndAttendingBetween
            (Integer capacityMin, Integer capacityMax, Integer attendingMin, Integer attendingMax, Pageable pageable);
    EventModel save(EventModel event);

    void delete(EventModel event);

    void deleteAll();
}