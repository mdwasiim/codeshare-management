package com.codeshare.airline.inbound.validations.validator.file;

import com.codeshare.airline.inbound.validations.model.ValidationResult;

public interface ScheduleFileExtensionValidation<T> {
    ValidationResult validate(T context);
}
