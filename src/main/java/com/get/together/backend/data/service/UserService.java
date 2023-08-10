package com.get.together.backend.data.service;

import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.repository.UserRepository;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import com.get.together.backend.data.validator.UserValidator;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserService {
    final UserRepository userRepository;
    final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public UserModel findById(Integer id) {
        try {
            if (Objects.isNull(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "userId must not be null");
        }
            val result = userRepository.findById(id);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with id: ".concat(id.toString()));
            }
            return result;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    GenericPagedModel<UserModel> findAllByUserNameContainingIgnoreCase
            (String userName, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByUserNameContainingIgnoreCase(userName, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByUserNameContainingIgnoreCase(userName, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with userName: ".concat(userName));
            }

            return GenericPagedModel.<UserModel>builder()
                    .totalElements(result.getTotalElements())
                    .numberOfElements(result.getNumberOfElements())
                    .totalPages(result.getTotalPages())
                    .content(result.getContent())
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public UserModel save(UserModel userModel) {
        try {
            userValidator.validate(userModel);
            if(userRepository.existsByUserName(userModel.getUserName()) && Objects.isNull(userModel.getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                        "UserName".concat(userModel.getUserName()).concat(" already in use"));
            }
            return userRepository.save(userModel);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}
