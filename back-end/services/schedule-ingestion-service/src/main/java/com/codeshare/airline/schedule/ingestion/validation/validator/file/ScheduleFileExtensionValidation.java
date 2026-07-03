package com.codeshare.airline.schedule.ingestion.validation.validator.file;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;

import java.util.Set;

public interface ScheduleFileExtensionValidation<T> {

    Set<MessageType> supportedTypes();

    ValidationResult validate(T context);
}
