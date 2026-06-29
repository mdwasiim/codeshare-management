package com.codeshare.airline.inbound.validations.validator;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.validations.model.ValidationResult;

import java.util.Set;

public interface BusinessValidation<T> {

    Set<MessageType> supportedTypes();

    ValidationResult validate(T context);
}
