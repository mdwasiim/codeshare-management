package com.codeshare.airline.ingestion.validations.validator.file;

import com.codeshare.airline.ingestion.validations.model.ValidationResult;

public interface ScheduleFileExtensionValidation<T> {
    ValidationResult validate(T context);
}
