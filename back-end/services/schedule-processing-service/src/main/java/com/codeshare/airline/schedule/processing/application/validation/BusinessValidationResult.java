package com.codeshare.airline.schedule.processing.application.validation;

import java.util.List;

public record BusinessValidationResult(List<BusinessValidationIssue> issues) {

    public boolean hasErrors() {
        return issues != null && !issues.isEmpty();
    }
}
