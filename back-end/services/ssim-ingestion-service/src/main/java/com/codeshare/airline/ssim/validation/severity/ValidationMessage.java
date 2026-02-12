package com.codeshare.airline.ssim.validation.severity;


import lombok.Getter;

@Getter
public class ValidationMessage {

    private final ValidationSeverity severity;
    private final String message;

    public ValidationMessage(
            ValidationSeverity severity,
            String message
    ) {
        this.severity = severity;
        this.message = message;
    }
}
