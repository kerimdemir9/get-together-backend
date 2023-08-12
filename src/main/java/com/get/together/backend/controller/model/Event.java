package com.get.together.backend.controller.model;

import lombok.*;

import java.util.Collection;
import java.util.Date;
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    Integer id;
    String header;
    String description;
    Integer capacity;
    Integer attending;
    Date created;
    Boolean isActive;
    Integer hostId;
    Collection<User> attendees;
}
