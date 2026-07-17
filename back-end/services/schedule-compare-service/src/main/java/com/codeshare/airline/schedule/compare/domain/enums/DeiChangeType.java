package com.codeshare.airline.schedule.compare.domain.enums;

/**
 * Type of change on a segment DEI record.
 */
public enum DeiChangeType {

    ADDED,      // DEI code present in ingested but not in live
    REMOVED,    // DEI code present in live but not in ingested
    MODIFIED    // DEI code present in both but value changed
}
