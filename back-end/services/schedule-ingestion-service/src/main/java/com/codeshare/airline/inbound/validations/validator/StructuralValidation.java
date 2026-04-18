package com.codeshare.airline.inbound.validations.validator;

import com.codeshare.airline.inbound.validations.model.ValidationResult;

public interface StructuralValidation<T> {
    ValidationResult validate(T context);
}