package com.get.together.backend.controller.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Integer id;
    String userName;
    String firstName;
    String lastName;
    String phoneNumber;
    String biography;
    String mail;
    Date created;
}
