package com.get.together.backend.data.repository;

import com.get.together.backend.data.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, Integer> {
    UserModel findById(Integer id);
    Boolean existsByUserName(String userName);
    Boolean existsByMail(String mail);
    Page<UserModel> findAllByUserNameContainingIgnoreCase
            (String userName, Pageable pageable);
    Page<UserModel> findAllByFirstNameContainingIgnoreCase
            (String firstName, Pageable pageable);
    Page<UserModel> findAllByLastNameContainingIgnoreCase
            (String lastName, Pageable pageable);
    Page<UserModel> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase
            (String firstName, String lastName, Pageable pageable);
    Page<UserModel> findAllByMailContainingIgnoreCase(String mail, Pageable pageable);

    Page<UserModel> findAllByPhoneNumberContaining(String phoneNUmber, Pageable pageable);

    UserModel save(UserModel userModel);

    void delete(UserModel user);

    void deleteAll();
}
