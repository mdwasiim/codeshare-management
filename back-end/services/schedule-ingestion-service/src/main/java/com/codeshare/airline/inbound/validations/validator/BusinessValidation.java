package com.codeshare.airline.inbound.validations.validator;

import com.codeshare.airline.inbound.validations.model.ValidationResult;

public interface BusinessValidation<T> {
    ValidationResult validate(T context);
}