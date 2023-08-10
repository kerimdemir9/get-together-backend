package com.get.together.backend.validation.helper;

import com.get.together.backend.validation.SimpleValidation;
import com.get.together.backend.validation.Validation;

import java.util.Objects;

public class ObjectValidationHelpers {
    public static Validation<Object> notNull = SimpleValidation.from(Objects::nonNull, "must not be null.");
}
