package com.codeshare.airline.schedule.domain.common;

public enum ScheduleProfile {
    OPERATIONAL,   // T1–T5 (codeshare, schedules)
    PLANNING,      // R1–R6 (seasonal planning)
    MIXED,         // both present (rare but possible)
    UNKNOWN
}
