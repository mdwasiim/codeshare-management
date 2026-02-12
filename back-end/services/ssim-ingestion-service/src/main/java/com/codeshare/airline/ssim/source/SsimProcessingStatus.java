package com.codeshare.airline.ssim.source;

public enum SsimProcessingStatus {

    RECEIVED,    // File metadata created, stream not yet read
    DETECTED,    // SSIM profile detected (OPERATIONAL / PLANNING)
    LOADING,     // Streaming ingestion in progress
    PARTIAL,     // Some flights committed, then failure
    COMPLETED,   // Fully ingested successfully
    FAILED,       // Failed before any usable persistence
    STORED
}
