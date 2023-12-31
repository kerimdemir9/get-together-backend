package com.get.together.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event", schema = "get_together")
@Entity
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String header;

    String description;

    Integer capacity;

    Integer attending;

    Date created;

    @Column(name = "is_active")
    Boolean isActive;

    @ManyToOne
    @JsonBackReference
    UserModel host;

    @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    Collection<UserModel> attendees;


}
