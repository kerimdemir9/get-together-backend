package com.get.together.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment", schema = "get_together")
@Entity
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String description;

    Date created;

    @ManyToOne
    @JsonBackReference
    UserModel owner;

    @ManyToOne
    @JsonBackReference
    EventModel relatedEvent;
}
