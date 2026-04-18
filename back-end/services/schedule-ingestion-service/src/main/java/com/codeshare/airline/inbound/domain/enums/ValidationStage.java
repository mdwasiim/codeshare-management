package com.codeshare.airline.inbound.domain.enums;

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