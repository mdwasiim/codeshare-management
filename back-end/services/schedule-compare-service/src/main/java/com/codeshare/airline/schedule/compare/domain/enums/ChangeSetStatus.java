package com.codeshare.airline.schedule.compare.domain.enums;

/**
 * Tracks the lifecycle of a detected change inside the generated change set.
 */
public enum ChangeSetStatus {

    PENDING,        // Change detected and waiting for manual approval
    AUTO_ACCEPTED,  // Change accepted by partner-level acceptance rule
    APPLIED,        // Change carried forward as the effective change-set entry
    REJECTED,       // Change rejected (e.g. duplicate, conflict, manual override)
    FAILED,         // Change-set processing failed for this entry
    SUPERSEDED      // A newer change for the same business key replaced this entry
}

