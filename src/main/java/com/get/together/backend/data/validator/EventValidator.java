package com.get.together.backend.data.validator;

import com.get.together.backend.data.model.EventModel;
import com.get.together.backend.data.model.UserModel;

public interface EventValidator {
    void validate(EventModel model);
}
