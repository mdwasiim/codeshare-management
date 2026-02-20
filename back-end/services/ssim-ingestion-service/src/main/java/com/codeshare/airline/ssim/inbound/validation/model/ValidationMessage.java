package com.codeshare.airline.ssim.inbound.validation.model;


import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ValidationMessage {

    private final UUID id;
    private final ValidationSeverity severity;
    private final String message;
    private final String code;
    private final boolean blocking;
    private final Instant createdAt;

    private UUID loadId;
    private String fileId;
    private String recordType;
    private String recordKey;

    public ValidationMessage(
            String code,
            String message,
            ValidationSeverity severity,
            boolean blocking) {

        this.id = UUID.randomUUID();
        this.code = code;
        this.message = message;
        this.severity = severity;
        this.blocking = blocking;
        this.createdAt = Instant.now();
    }

    // ---------- Factory Methods ----------

    public static ValidationMessage of(
            String code,
            String message,
            ValidationSeverity severity,
            boolean blocking) {

        return new ValidationMessage(code, message, severity, blocking);
    }

    public static ValidationMessage blocking(
            String code,
            String message) {

        return new ValidationMessage(
                code,
                message,
                ValidationSeverity.BLOCKING,
                true
        );
    }

    public static ValidationMessage warning(
            String code,
            String message) {

        return new ValidationMessage(
                code,
                message,
                ValidationSeverity.WARNING,
                false
        );
    }

    public static ValidationMessage error(
            String code,
            String message) {

        return new ValidationMessage(
                code,
                message,
                ValidationSeverity.ERROR,
                true
        );
    }
}
