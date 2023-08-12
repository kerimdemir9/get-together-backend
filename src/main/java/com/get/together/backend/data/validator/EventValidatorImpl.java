package com.get.together.backend.data.validator;

import com.get.together.backend.data.model.EventModel;
import org.springframework.stereotype.Component;

import static com.get.together.backend.validation.Constants.*;
import static com.get.together.backend.validation.helper.IntegerValidationHelpers.greaterThan;
import static com.get.together.backend.validation.helper.IntegerValidationHelpers.notNullInteger;
import static com.get.together.backend.validation.helper.StringValidationHelpers.*;

@Component
public class EventValidatorImpl implements EventValidator{
    @Override
    public void validate(EventModel model) {
        notBlank.test(model.getHeader()).throwIfInvalid(EVENT_HEADER_FIELD_FOR_VALIDATION);
        notNullInteger.and(greaterThan(1)).test(model.getCapacity())
                .throwIfInvalid(EVENT_CAPACITY_FIELD_FOR_VALIDATION);
    }
}
