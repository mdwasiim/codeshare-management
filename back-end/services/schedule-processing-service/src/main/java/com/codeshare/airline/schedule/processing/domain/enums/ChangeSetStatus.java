package com.codeshare.airline.schedule.processing.domain.enums;

/**
 * Tracks the lifecycle of a detected change inside the generated change set.
 */
public enum ChangeSetStatus {

    PENDING,        // Change detected and recorded during comparison
    APPLIED,        // Change carried forward as the effective change-set entry
    REJECTED,       // Change rejected (e.g. duplicate, conflict, manual override)
    FAILED,         // Change-set processing failed for this entry
    SUPERSEDED      // A newer change for the same business key replaced this entry
}

