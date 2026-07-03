package com.codeshare.airline.schedule.ingestion.validation.validator;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;

import java.util.Set;

public interface BusinessValidation<T> {

    Set<MessageType> supportedTypes();

    ValidationResult validate(T context);
}
