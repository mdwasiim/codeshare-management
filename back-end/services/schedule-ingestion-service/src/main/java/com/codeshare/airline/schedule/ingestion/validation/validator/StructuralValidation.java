package com.codeshare.airline.schedule.ingestion.validation.validator;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;

import java.util.Set;

public interface StructuralValidation<T> {

    Set<MessageType> supportedTypes();

    ValidationResult validate(T context);

    @SuppressWarnings("unchecked")
    default ValidationResult validateContext(AbstractIngestionContext<?, ?> context) {
        return validate((T) context);
    }
}
