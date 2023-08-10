package com.get.together.backend.controller;

import com.get.together.backend.controller.model.PagedData;
import com.get.together.backend.controller.model.User;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.service.UserService;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.CryptographyUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/v1/user/save", method = RequestMethod.POST)
    private ResponseEntity<User> saveUserV1(@RequestBody User user) {
        log.info("Calling: saveUserV1 >> ".concat(user.toString()));

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

    private PagedData<User> mapPaged(GenericPagedModel<UserModel> model) {
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
