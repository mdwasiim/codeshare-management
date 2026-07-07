package com.codeshare.airline.schedule.live.domain.enums;

/**
 * Origin source that produced or last updated a live flight leg.
 *
 * <pre>
 *  SSIM   – Full seasonal schedule file (Chapter 4 SSIM T3 records).
 *  SSM    – Schedule change message (Chapter 7, point-in-time update).
 *  ASM    – Ad-hoc schedule message (Chapter 7, operational ad-hoc change).
 *  MANUAL – Operator-entered correction via management UI.
 * </pre>
 */
public enum LiveScheduleSource {

    SSIM,
    SSM,
    ASM,
    MANUAL
}
