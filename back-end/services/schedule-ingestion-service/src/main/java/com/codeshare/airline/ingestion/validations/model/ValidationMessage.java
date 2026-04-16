package com.codeshare.airline.ingestion.validations.model;


import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import lombok.Getter;

@Getter
public class ValidationMessage {

    private final String ruleCode;
    private final String message;
    private final String recordType;
    private final String recordKey;
    private final ValidationSeverity severity;
    private final ValidationStage stage;

    private ValidationMessage(String ruleCode,
                              String message,
                              String recordType,
                              String recordKey,
                              ValidationSeverity severity,
                              ValidationStage stage) {

        this.ruleCode = ruleCode;
        this.message = message;
        this.recordType = recordType;
        this.recordKey = recordKey;
        this.severity = severity;
        this.stage = stage;
    }

    // =========================
    // FACTORY METHODS
    // =========================

    public static ValidationMessage error(String code,
                                          String message,
                                          String recordType,
                                          String recordKey,
                                          ValidationStage stage) {

        return new ValidationMessage(code, message, recordType, recordKey,ValidationSeverity.ERROR, stage);
    }

    public static ValidationMessage warning(String code,
                                            String message,
                                            String recordType,
                                            String recordKey,
                                            ValidationStage stage) {

        return new ValidationMessage(code, message, recordType, recordKey,
                ValidationSeverity.WARNING, stage);
    }

    public static ValidationMessage info(String code,
                                         String message,
                                         String recordType,
                                         String recordKey,
                                         ValidationStage stage) {

        return new ValidationMessage(code, message, recordType, recordKey,
                ValidationSeverity.INFO, stage);
    }

    public static ValidationMessage parsingError(String details) {

        return new ValidationMessage(
                "PARSING_ERROR",
                details != null ? details : "Parsing failed",
                null,
                null,
                ValidationSeverity.ERROR,
                ValidationStage.PARSING
        );
    }

    public static ValidationMessage systemError(String message) {

        return new ValidationMessage(
                "SYSTEM_ERROR",
                message,
                null,
                null,
                ValidationSeverity.ERROR,
                ValidationStage.PROCESSING
        );
    }

    // =========================
    // HELPERS
    // =========================

    public boolean isError() {
        return severity == ValidationSeverity.ERROR;
    }

    public boolean isWarning() {
        return severity == ValidationSeverity.WARNING;
    }

    public boolean isInfo() {
        return severity == ValidationSeverity.INFO;
    }
}