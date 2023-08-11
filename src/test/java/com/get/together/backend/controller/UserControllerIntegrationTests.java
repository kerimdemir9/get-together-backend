package com.get.together.backend.controller;

import com.get.together.backend.GetTogetherBackendApplication;
import com.get.together.backend.TestBase;
import com.get.together.backend.controller.model.User;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.util.GenericPagedModel;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

public class UserControllerIntegrationTests extends TestBase {
    private static UserModel newUser;
    private static UserModel newUser2;

    public void insertNewUser() {
        newUser = userService.save(UserModel.builder()
                .userName("user1")
                .mail("user@gmail.com")
                .firstName("F1")
                .lastName("L1")
                .password("P1")
                .biography("")
                .phoneNumber("12345")
                .created(new Date(Instant.now().toEpochMilli()))
                .build());
    }

    public void insertNewUser2() {
        newUser2 = userService.save(UserModel.builder()
                .userName("user2")
                .mail("user2@gmail.com")
                .firstName("F2")
                .lastName("L2")
                .password("P2")
                .biography("")
                .phoneNumber("123456")
                .created(new Date(Instant.now().toEpochMilli()))
                .build());
    }

    @Before
    public void setup() {
        userService.hardDeleteAll();
    }

    @Test
    public void find_by_id_test() {
        insertNewUser();

        val url = TestBase.LOCALHOST
                .concat(String.valueOf(port))
                .concat("/v1/user/")
                .concat(newUser.getId().toString());
        val response = restTemplate.getForEntity(url, User.class);

        assertTrue(StringUtils.isNotBlank(response.toString()));
        assertNotNull(response.getBody());

        assertEquals(newUser.getId(),response.getBody().getId());
    }
}
