package com.codeshare.airline.schedule.processing.domain.enums;

public enum ProcessingJobStatus {
    REQUESTED,
    VALIDATING,
    VALIDATION_FAILED,
    VALIDATED,
    COMPARE_REQUESTED,
    FAILED
}
