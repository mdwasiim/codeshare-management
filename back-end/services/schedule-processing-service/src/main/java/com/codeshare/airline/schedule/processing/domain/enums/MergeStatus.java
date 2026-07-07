package com.codeshare.airline.schedule.processing.domain.enums;

/**
 * Tracks whether a detected change has been merged back into schedule-service.
 */
public enum MergeStatus {

    PENDING,        // Change detected, not yet applied to live schedule
    APPLIED,        // Change successfully merged into schedule-service
    REJECTED,       // Change rejected (e.g. duplicate, conflict, manual override)
    FAILED,         // Merge attempted but failed (technical error)
    SUPERSEDED      // A newer change for the same leg was applied instead
}
