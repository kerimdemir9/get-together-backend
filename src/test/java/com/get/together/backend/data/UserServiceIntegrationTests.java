package com.get.together.backend.data;


import com.get.together.backend.TestBase;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

public class UserServiceIntegrationTests extends TestBase {

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

    @Test
    public void insert_new_user_test() {
        insertNewUser();

        assertNotNull(newUser);
        assertEquals("user1", newUser.getUserName());
    }

    @Test(expected = ResponseStatusException.class)
    public void insert_new_user_with_exception_test() {
        val userToInsert = UserModel.builder()
                .userName("kerim").build();
        userService.save(userToInsert);
    }

    @Test
    public void delete_user_test() {
        insertNewUser();
        val deleted = userService.hardDelete(newUser.getId());

        assertEquals(deleted.getId(), newUser.getId());
        assertEquals(deleted.getUserName(), newUser.getUserName());
    }

    @Test(expected = ResponseStatusException.class)
    public void delete_user_with_exception_test() {
        insertNewUser();

        userService.hardDelete(newUser.getId() + 1);
    }

    @Test
    public void update_user_test() {
        insertNewUser();

        val updated = userService.save(UserModel.builder()
                .id(newUser.getId())
                .userName("kerim")
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .password(newUser.getPassword())
                .biography(newUser.getBiography())
                .phoneNumber(newUser.getPhoneNumber())
                .created(newUser.getCreated())
                .mail(newUser.getMail())
                .build());
        assertEquals(updated.getUserName(), "kerim");
        assertEquals(updated.getId(), newUser.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void update_user_with_exception_test() {
        insertNewUser();

        userService.save(UserModel.builder()
                .id(newUser.getId())
                .firstName("halil")
                .build());
    }

    @Test
    public void find_by_id_test() {
        insertNewUser();

        val found = userService.findById(newUser.getId());

        assertNotNull(found);
        assertEquals(found.getId(), newUser.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void find_by_id_with_exception_test() {
        insertNewUser();

        userService.findById(newUser.getId() + 1);
    }

    @Test
    public void find_all_like_first_name_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByFirstNameContainingIgnoreCase
                ("F", 0, 10, "id", SortDirection.Ascending));
    }

    @Test(expected = ResponseStatusException.class)
    public void find_all_like_first_name_with_exception_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByFirstNameContainingIgnoreCase
                ("A", 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_last_name_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByLastNameContainingIgnoreCase
                ("L", 0, 10, "id", SortDirection.Ascending));
    }

    @Test(expected = ResponseStatusException.class)
    public void find_all_like_last_name_with_exception_test() {
        insertNewUser();

        userService.findAllByLastNameContainingIgnoreCase
                ("A", 0, 10, "id", SortDirection.Ascending);
    }

    @Test
    public void find_all_like_first_and_last_name_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
                ("F", "L", 0, 10, "id", SortDirection.Ascending));
    }

    @Test(expected = ResponseStatusException.class)
    public void find_all_like_first_and_last_name_with_exception_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
                ("B", "B", 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_mail_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByMailContainingIgnoreCase
                ("@gmail.com", 0, 10, "id", SortDirection.Ascending));
    }

    @Test(expected = ResponseStatusException.class)
    public void find_all_like_mail_with_exception_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByMailContainingIgnoreCase
                ("@hotmail.com", 0, 10, "id", SortDirection.Ascending));
    }

    @Test
    public void find_all_like_phone_number_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByPhoneNumberContaining
                ("123", 0, 10, "id", SortDirection.Ascending));
    }

    @Test(expected = ResponseStatusException.class)
    public void find_all_like_phone_number_with_exception_test() {
        insertNewUser();
        insertNewUser2();

        testCollection(userService.findAllByPhoneNumberContaining
                ("000", 0, 10, "id", SortDirection.Ascending));
    }
}
