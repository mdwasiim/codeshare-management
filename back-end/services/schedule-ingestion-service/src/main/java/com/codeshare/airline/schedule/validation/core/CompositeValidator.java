package com.codeshare.airline.schedule.validation.core;

import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;

import java.util.List;

public class CompositeValidator<T> implements Validator<T> {

    private final List<Validator<T>> validators;

    public CompositeValidator(List<Validator<T>> validators) {
        this.validators = validators;
    }

    @Override
    public ValidationResult validate(T target) {

        List<ValidationMessage> messages = validators.stream()
                .map(v -> v.validate(target))
                .flatMap(r -> r.getMessages().stream())
                .toList();

        return ValidationResult.of(messages);
    }
}