package com.codeshare.airline.schedule.validation.model;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {

    private final List<ValidationMessage> messages;

    public ValidationResult(List<ValidationMessage> messages) {
        this.messages = messages == null
                ? Collections.emptyList()
                : List.copyOf(messages);
    }

    public static ValidationResult valid() {
        return new ValidationResult(Collections.emptyList());
    }

    public static ValidationResult of(List<ValidationMessage> messages) {
        return new ValidationResult(messages);
    }

    public boolean hasBlockingErrors() {
        return messages.stream()
                .anyMatch(ValidationMessage::isBlocking);
    }

    public boolean hasErrors() {
        return !messages.isEmpty();
    }

    public boolean isValid() {
        return messages.isEmpty();
    }
}
