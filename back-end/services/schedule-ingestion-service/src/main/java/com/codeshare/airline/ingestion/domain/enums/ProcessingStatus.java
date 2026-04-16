package com.codeshare.airline.ingestion.domain.enums;

public enum ProcessingStatus {

    RECEIVED,
    REJECTED,

    VALIDATING,     // file-level validation
    PROCESSING,     // stream running

    COMPLETED,      // all messages successful
    PARTIAL,        // some failed
    FAILED,          // critical failure
    SUCCESS
}