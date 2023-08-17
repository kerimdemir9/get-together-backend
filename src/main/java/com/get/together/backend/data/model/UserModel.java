package com.get.together.backend.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "user", schema = "get_together")
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "user_name")
    String userName;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "phone_number")
    String phoneNumber;

    String biography;

    String mail;

    Date created;

    String password;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    Collection<EventModel> hostedEvents;

    @ManyToMany(mappedBy = "attendees")
    @JsonManagedReference
    @ToString.Exclude
    Collection<EventModel> attendedEvents;
}
