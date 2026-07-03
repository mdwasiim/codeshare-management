package com.codeshare.airline.schedule.ingestion.validation.engine;

import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;


@FunctionalInterface
public interface ValidatorExecutor<T, V> {
    ValidationResult execute(V validator, T target);
}