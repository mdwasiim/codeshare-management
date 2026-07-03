package com.codeshare.airline.schedule.ingestion.domain.enums;

public enum ValidationStage {
    FILE_TYPE,
    PROCESSING,
    STRUCTURAL,
    PARSING,
    VALIDATION,
    BUSINESS,
    EXCEPTION,
    TOLERANT
}