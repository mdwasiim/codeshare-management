package com.codeshare.airline.ssim.inbound.validation.model;

import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final List<ValidationMessage> messages;

    public ValidationResult(List<ValidationMessage> messages) {
        this.messages = messages == null
                ? Collections.emptyList()
                : List.copyOf(messages);
    }

    public List<ValidationMessage> getMessages() {
        return messages;
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
