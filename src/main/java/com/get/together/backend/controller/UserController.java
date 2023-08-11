package com.get.together.backend.controller;

import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.controller.model.User;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.service.UserService;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.CryptographyUtil;
import com.get.together.backend.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.get.together.backend.controller.util.Parsers.tryParseInteger;

@Controller
@Slf4j
public class UserController {

    final UserService userService;
    final CryptographyUtil cryptographyUtil;

    @Autowired
    public UserController(UserService userService, CryptographyUtil cryptographyUtil) {
        this.userService = userService;
        this.cryptographyUtil = cryptographyUtil;
    }

    @RequestMapping(value = "/v1/user/{id}", method = RequestMethod.GET)
    private ResponseEntity<User> getUserByIdV1(@PathVariable String id) {
        log.info("Calling: getUserByIdV1 >> ".concat(id));

        val response = userService.findById(tryParseInteger(id, "id"));

        return ResponseEntity.ok(mapUser(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_user_name/{userName}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikeUserNameV1
            (@PathVariable String userName,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: geUserLikeUserNameV1 >> ".concat(userName));

        val response = userService.findAllByUserNameContainingIgnoreCase
                (userName, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_first_name/{firstName}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikeFirstNameV1
            (@PathVariable String firstName,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getUserLikeFirstNameV1 >> ".concat(firstName));

        val response = userService.findAllByFirstNameContainingIgnoreCase
                (firstName, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/find_by_user_name/{userName}", method = RequestMethod.GET)
    private ResponseEntity<User> getUserByUserNameV1(@PathVariable String userName) {
        log.info("Calling: getUserByUserNameV1 >> ".concat(userName));

        val response = userService.findByUserName(userName);

        return ResponseEntity.ok(mapUser(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_last_name/{lastName}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikeLastNameV1
            (@PathVariable String lastName,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getUserLikeLastNameV1 >> ".concat(lastName));

        val response = userService.findAllByLastNameContainingIgnoreCase
                (lastName, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_first_name_and_last_name/{firstName}/{lastName}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikeFirstAndLastNameV1
            (@PathVariable String firstName,
             @PathVariable String lastName,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getUserLikeFirstAndLastNameV1 >> ".concat(lastName));

        val response = userService.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
                (firstName, lastName, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_mail/{mail}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikeMailV1
            (@PathVariable String mail,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getUserLikeMailV1 >> ".concat(mail));

        val response = userService.findAllByMailContainingIgnoreCase
                (mail, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/find_all_like_phone_number/{phoneNumber}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<User>> getUserLikePhoneNumberV1
            (@PathVariable String phoneNumber,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Calling: getUserLikePhoneNumberV1 >> ".concat(phoneNumber));

        val response = userService.findAllByPhoneNumberContaining
                (phoneNumber, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/user/save", method = RequestMethod.POST)
    private ResponseEntity<User> saveUserV1(@RequestBody User user) {
        log.info("Calling: saveUserV1 >> ".concat(user.toString()));

        val response = userService.save(UserModel.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .biography(user.getBiography())
                .mail(user.getMail())
                .created(new Date(Instant.now().toEpochMilli()))
                .password(cryptographyUtil.encrypt(user.getPassword()))
                .build());

        return ResponseEntity.ok(mapUser(response));
    }

    @RequestMapping(value = "/v1/user/update", method = RequestMethod.POST)
    private ResponseEntity<User> updateUserV1(@RequestBody User user) {
        log.info("Calling: updateUserV1 >> ".concat(user.toString()));

        val response = userService.save(UserModel.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .biography(user.getBiography())
                .mail(user.getMail())
                .created(new Date(Instant.now().toEpochMilli()))
                .password(cryptographyUtil.encrypt(user.getPassword()))
                .build());

        return ResponseEntity.ok(mapUser(response));
    }

    @RequestMapping(value = "/v1/user/delete/{userId}", method = RequestMethod.DELETE)
    private ResponseEntity<User> deleteUserV1(@PathVariable Integer userId) {
        log.info("Calling: deleteUserV1 >> ".concat(userId.toString()));

        val response = userService.hardDelete(userId);

        return ResponseEntity.ok(mapUser(response));
    }

    private PagedData<User> mapPagedData(GenericPagedModel<UserModel> model) {
        return PagedData.<User>builder()
                .totalElements(model.getTotalElements())
                .numberOfElements(model.getNumberOfElements())
                .totalPages(model.getTotalPages())
                .content(mapUsers(model.getContent()))
                .build();
    }

    private Collection<User> mapUsers(Collection<UserModel> models) {
        return new ArrayList<>(models.stream().map(this::mapUser).toList());
    }

    private User mapUser(UserModel model) {
        return User.builder()
                .id(model.getId())
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .phoneNumber(model.getPhoneNumber())
                .biography(model.getBiography())
                .mail(model.getMail())
                .created(model.getCreated())
                .password(cryptographyUtil.decrypt(model.getPassword()))
                .build();
    }
}
