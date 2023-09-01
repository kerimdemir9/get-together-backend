package com.get.together.backend.data.repository;


import com.get.together.backend.data.model.CommentModel;
import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<CommentModel, Integer> {
    Page<CommentModel> findAllByOwner(UserModel ownerId, Pageable pageable);
    Page<CommentModel> findAllByRelatedEvent(EventModel relatedEventId, Pageable pageable);
    Page<CommentModel> findAllByOwnerAndRelatedEvent
            (UserModel ownerId, EventModel relatedEventId, Pageable pageable);
    Page<CommentModel> findAllByCreatedBeforeAndCreatedAfter
            (Date createdBefore, Date createdAfter, Pageable pageable);
    Page<CommentModel> findAllByOwnerAndCreatedBeforeAndCreatedAfter
            (UserModel ownerId, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<CommentModel> findAllByOwnerAndRelatedEventAndCreatedBeforeAndCreatedAfter
            (UserModel ownerId, EventModel relatedEventId, Date createdBefore, Date createdAfter, Pageable pageable);
}
