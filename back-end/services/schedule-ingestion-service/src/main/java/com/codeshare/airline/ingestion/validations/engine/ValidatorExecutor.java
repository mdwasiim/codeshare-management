package com.codeshare.airline.ingestion.validations.engine;

import com.codeshare.airline.ingestion.validations.model.ValidationResult;


@FunctionalInterface
public interface ValidatorExecutor<T, V> {
    ValidationResult execute(V validator, T target);
}