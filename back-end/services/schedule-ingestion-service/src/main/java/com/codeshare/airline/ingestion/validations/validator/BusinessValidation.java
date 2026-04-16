package com.codeshare.airline.ingestion.validations.validator;

import com.codeshare.airline.ingestion.validations.model.ValidationResult;

public interface BusinessValidation<T> {
    ValidationResult validate(T context);
}