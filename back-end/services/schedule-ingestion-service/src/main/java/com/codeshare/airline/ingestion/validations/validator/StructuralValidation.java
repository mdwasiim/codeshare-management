package com.codeshare.airline.ingestion.validations.validator;

import com.codeshare.airline.ingestion.validations.model.ValidationResult;

public interface StructuralValidation<T> {
    ValidationResult validate(T context);
}