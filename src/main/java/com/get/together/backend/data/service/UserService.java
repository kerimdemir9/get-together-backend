package com.get.together.backend.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.get.together.backend.data.model.UserModel;
import com.get.together.backend.data.repository.UserRepository;
import com.get.together.backend.data.util.GenericPagedModel;
import com.get.together.backend.util.SortDirection;
import com.get.together.backend.data.validator.UserValidator;
import lombok.Data;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
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
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with id: ".concat(id.toString()));
            }
            return result.get();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public GenericPagedModel<UserModel> findAllByUserNameContainingIgnoreCase
            (String userName, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByUserNameContainingIgnoreCase
                    (userName, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByUserNameContainingIgnoreCase
                    (userName, PageRequest.of(page, size, Sort.by(sortBy).descending()));
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

    public GenericPagedModel<UserModel> findAllByFirstNameContainingIgnoreCase
            (String firstName, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByFirstNameContainingIgnoreCase
                    (firstName, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByFirstNameContainingIgnoreCase
                    (firstName, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with firstName: ".concat(firstName));
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

    public GenericPagedModel<UserModel> findAllByLastNameContainingIgnoreCase
            (String lastName, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByLastNameContainingIgnoreCase
                    (lastName, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByLastNameContainingIgnoreCase
                    (lastName, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with lastName: ".concat(lastName));
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

    public GenericPagedModel<UserModel> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
            (String firstName, String lastName, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
                    (firstName, lastName, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
                    (firstName, lastName, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with firstName: ".concat(firstName)
                        .concat(" and lastName: ").concat(lastName));
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

    public GenericPagedModel<UserModel> findAllByMailContainingIgnoreCase
            (String mail, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByMailContainingIgnoreCase
                    (mail, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByMailContainingIgnoreCase
                    (mail, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with mail: ".concat(mail));
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

    public GenericPagedModel<UserModel> findAllByPhoneNumberContaining
            (String phoneNumber, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? userRepository.findAllByPhoneNumberContaining
                    (phoneNumber, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : userRepository.findAllByPhoneNumberContaining
                    (phoneNumber, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with phoneNumber: ".concat(phoneNumber));
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

    public UserModel findByUserName(String userName) {
        try {
            val result = userRepository.findByUserName(userName);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with this userName: ".concat(userName));
            }
            return result;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public UserModel save(UserModel userModel) {
        try {
            userValidator.validate(userModel);
            val userName = userRepository.existsByUserName(userModel.getUserName());
            val mail = userRepository.existsByMail(userModel.getMail());
            if(Objects.isNull(userModel.getId()) && (userName || mail)) {
                if(userName) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                            "UserName: ".concat(userModel.getUserName()).concat(" already in use"));
                }
                else {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                            "Mail: ".concat(userModel.getMail()).concat(" already in use"));
                }
            }
            userModel.setCreated(new Date(Instant.now().toEpochMilli()));
            return userRepository.save(userModel);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public UserModel hardDelete(Integer id) {
        try {
            val userToHardDelete = findById(id);

            userRepository.delete(userToHardDelete);

            return userToHardDelete;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void hardDeleteAll() {
        try {
            userRepository.deleteAll();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
