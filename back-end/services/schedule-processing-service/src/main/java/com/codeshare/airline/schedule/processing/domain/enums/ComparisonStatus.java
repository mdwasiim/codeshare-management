package com.codeshare.airline.schedule.processing.domain.enums;

/**
 * Status of a schedule comparison run.
 */
public enum ComparisonStatus {

    PENDING,        // Run created, comparison not yet started
    IN_PROGRESS,    // Comparison currently running
    COMPLETED,      // Comparison finished, changes recorded
    FAILED,         // Comparison failed due to error
    SKIPPED         // No live schedule found to compare against — e.g. first-ever load
}
