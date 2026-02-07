package com.codeshare.airline.processor.pipeline.enm;
/**
 * Enumeration of SSIM load processing statuses.
 */
public enum SsimLoadStatus {

    /**
     * Raw SSIM file received and persisted.
     * Parsing completed, but not yet validated.
     */
    LOADED,

    /**
     * SSIM structure validated (R1–R6 ordering, dates, integrity).
     * Safe for business processing.
     */
    VALIDATED,

    /**
     * Operating schedules expanded (Phase-1).
     */
    EXPANDED,

    /**
     * Marketing schedules expanded & conflicts resolved (Phase-2).
     * End of pipeline.
     */
    COMPLETED,

    /**
     * Load failed during parsing, validation, or processing.
     */
    FAILED,

    PERSISTED
}
