package com.get.together.backend.data.validator;

import com.get.together.backend.data.model.UserModel;
import org.springframework.stereotype.Component;

import static com.get.together.backend.validation.Constants.USER_FIRST_NAME_FIELD_FOR_VALIDATION;
import static com.get.together.backend.validation.Constants.USER_LAST_NAME_FIELD_FOR_VALIDATION;
import static com.get.together.backend.validation.Constants.USER_MAIL_FIELD_FOR_VALIDATION;
import static com.get.together.backend.validation.Constants.USER_USERNAME_FIELD_FOR_VALIDATION;
import static com.get.together.backend.validation.helper.StringValidationHelpers.notBlank;
import static com.get.together.backend.validation.helper.StringValidationHelpers.notValidEmail;

@Component
public class UserValidatorImpl implements UserValidator {
    @Override
    public void validate(UserModel model) {
        notBlank.test(model.getFirstName()).throwIfInvalid(USER_FIRST_NAME_FIELD_FOR_VALIDATION);
        notBlank.test(model.getLastName()).throwIfInvalid(USER_LAST_NAME_FIELD_FOR_VALIDATION);
        notBlank.test(model.getUserName()).throwIfInvalid(USER_USERNAME_FIELD_FOR_VALIDATION);
        notBlank.test(model.getMail()).throwIfInvalid(USER_MAIL_FIELD_FOR_VALIDATION);
        notValidEmail.test(model.getMail()).throwIfInvalid(USER_MAIL_FIELD_FOR_VALIDATION.concat(":")
                .concat(model.getMail()));
    }
}
