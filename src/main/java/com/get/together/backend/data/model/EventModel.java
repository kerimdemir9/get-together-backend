package com.get.together.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jdk.jfr.Frequency;
import lombok.*;

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

    Boolean isActive;

    @ManyToOne
    @JsonBackReference
    UserModel user;
}
