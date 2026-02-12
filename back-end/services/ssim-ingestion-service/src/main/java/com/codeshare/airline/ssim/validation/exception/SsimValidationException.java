package com.codeshare.airline.ssim.validation.exception;

import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;

public class SsimValidationException extends RuntimeException {

    private final ValidationSeverity severity;

    public SsimValidationException(
            ValidationSeverity severity,
            String message
    ) {
        super(message);
        this.severity = severity;
    }

    public ValidationSeverity getSeverity() {
        return severity;
    }
}
