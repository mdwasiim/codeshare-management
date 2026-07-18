package com.codeshare.airline.schedule.processing.application.validation;

public record BusinessValidationIssue(
        String ruleCode,
        String message,
        String recordType,
        String recordKey
) {
}
