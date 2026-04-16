package com.codeshare.airline.ingestion.validations.model;

import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {

    private final List<ValidationMessage> messages;

    public ValidationResult() {
        this.messages = new ArrayList<>();
    }

    public void add(ValidationMessage message) {
        messages.add(message);
    }

    // =========================
    // ERROR CHECKS
    // =========================

    public boolean hasErrors() {
        return messages.stream()
                .anyMatch(m -> m.getSeverity() == ValidationSeverity.ERROR);
    }

    public boolean hasErrors(ValidationStage stage) {
        return messages.stream()
                .anyMatch(m ->
                        m.getSeverity() == ValidationSeverity.ERROR &&
                                m.getStage() == stage
                );
    }

    // =========================
    // MERGE
    // =========================

    public void merge(ValidationResult other) {
        if (other == null) return;
        this.messages.addAll(other.messages);
    }

    public List<ValidationMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    // =========================
    // ADD METHODS (FIXED WITH STAGE)
    // =========================

    public void addError(String code, String message,
                         String recordType, String recordKey,
                         ValidationStage stage) {

        messages.add(ValidationMessage.error(
                code, message, recordType, recordKey,
                stage
        ));
    }

    public void addWarning(String code, String message,
                           String recordType, String recordKey,
                           ValidationStage stage) {
        messages.add(ValidationMessage.warning(code, message, recordType, recordKey,stage));
    }

    public void addInfo(String code, String message,
                        String recordType, String recordKey,
                        ValidationStage stage) {

        messages.add(ValidationMessage.info(
                code, message, recordType, recordKey,
                stage
        ));
    }

    // =========================
    // COUNTERS
    // =========================

    public long warningCount() {
        return messages.stream()
                .filter(m -> m.getSeverity() == ValidationSeverity.WARNING)
                .count();
    }

    public long errorCount() {
        return messages.stream()
                .filter(m -> m.getSeverity() == ValidationSeverity.ERROR)
                .count();
    }

    public long infoCount() {
        return messages.stream()
                .filter(m -> m.getSeverity() == ValidationSeverity.INFO)
                .count();
    }

    // =========================
    // OPTIONAL (VERY USEFUL)
    // =========================

    public List<ValidationMessage> getByStage(ValidationStage stage) {
        return messages.stream()
                .filter(m -> m.getStage() == stage)
                .toList();
    }
}