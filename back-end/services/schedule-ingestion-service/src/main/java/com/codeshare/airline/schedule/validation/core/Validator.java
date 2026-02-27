package com.codeshare.airline.schedule.validation.core;

import com.codeshare.airline.schedule.validation.model.ValidationResult;

public interface Validator<T> {

    ValidationResult validate(T target);
}