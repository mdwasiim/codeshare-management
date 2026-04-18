package com.codeshare.airline.inbound.validations.engine;

import com.codeshare.airline.inbound.validations.model.ValidationResult;


@FunctionalInterface
public interface ValidatorExecutor<T, V> {
    ValidationResult execute(V validator, T target);
}