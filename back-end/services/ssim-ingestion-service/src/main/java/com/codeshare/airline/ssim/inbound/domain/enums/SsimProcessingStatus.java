package com.codeshare.airline.ssim.inbound.domain.enums;

public enum SsimProcessingStatus {

    RECEIVED,              // File metadata created

    REJECTED,              // Invalid extension / idempotency failure

    DETECTING_PROFILE,     // Profile detection in progress
    PROFILE_DETECTED,      // Profile detected successfully

    STRUCTURAL_VALIDATING, // Structural validation in progress
    STRUCTURAL_FAILED,     // Blocking structural validation errors

    PARSING,               // Parsing + persistence in progress
    PARTIAL,               // Some flights persisted, then failure

    BUSINESS_VALIDATING,   // Business validation in progress
    BUSINESS_FAILED,       // Blocking business validation errors

    COMPLETED,             // Fully successful

    FAILED                 // System-level failure (unexpected exception)
}

