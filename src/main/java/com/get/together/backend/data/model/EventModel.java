package com.get.together.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Frequency;
import lombok.*;

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
    @JsonBackReference
    Collection<UserModel> attendees;
}
