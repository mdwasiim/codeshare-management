package com.codeshare.airline.ssim.inbound.domain.enums;

public enum SsimProfile {
    OPERATIONAL,   // T1–T5 (codeshare, schedules)
    PLANNING,      // R1–R6 (seasonal planning)
    MIXED,         // both present (rare but possible)
    UNKNOWN
}
