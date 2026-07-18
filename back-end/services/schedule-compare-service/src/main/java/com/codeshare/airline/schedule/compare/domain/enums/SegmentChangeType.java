package com.codeshare.airline.schedule.compare.domain.enums;

/**
 * Comparison result for a board/off-point segment on a leg.
 */
public enum SegmentChangeType {

    ADDED,      // Segment present in ingested data but not in live
    REMOVED,    // Segment present in live but absent in ingested data
    UNCHANGED,  // Segment exists in both — only DEI contents may have changed
    DEI_CHANGED // Segment itself unchanged but one or more DEI codes differ
}
