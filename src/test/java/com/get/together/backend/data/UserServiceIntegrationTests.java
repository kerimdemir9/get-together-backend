package com.get.together.backend.data;


import com.get.together.backend.TestBase;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserServiceIntegrationTests extends TestBase {

    private static UserModel newUser;
    private static UserModel newUser2;

    public void insertNewUser() {
        newUser = UserModel.builder()
                .userName("user1")
                .mail("mail1")
                .firstName("F1")
                .lastName("L1")
                .password("P1")
                .biography("")
                .phoneNumber("12345")
                .created(new Date(Instant.now().toEpochMilli()))
                .build();
    }

    public void insertNewUser2() {
        newUser2 = UserModel.builder()
                .userName("user2")
                .mail("mail2")
                .firstName("F2")
                .lastName("L2")
                .password("P2")
                .biography("")
                .phoneNumber("123456")
                .created(new Date(Instant.now().toEpochMilli()))
                .build();
    }


    public void testCollection(GenericPagedModel<UserModel> users) {
        assertFalse(users.getContent().isEmpty());

        assertTrue(users.getContent()
                .stream()
                .anyMatch(c -> c.getId().equals(newUser.getId())));

        assertTrue(users.getContent()
                .stream()
                .anyMatch(c -> c.getId().equals(newUser2.getId())));
    }

    @Before
    public void setup() {
        userService.hardDeleteAll();
    }

    @Test
    public void find_all_like_user_name_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByUserNameContainingIgnoreCase
                ("uSer", 0, 10, "id", SortDirection.Ascending));
    }
}
