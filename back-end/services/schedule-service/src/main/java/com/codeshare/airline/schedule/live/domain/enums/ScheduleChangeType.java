package com.codeshare.airline.schedule.live.domain.enums;

/**
 * Type of change applied to a live flight leg, aligned with SSIM Chapter 7
 * SSM/ASM action codes and Chapter 4/5 full-replacement semantics.
 *
 * <pre>
 *  NEW        – Brand new flight leg not previously in the live schedule (SSM: NEW).
 *  RETIMED    – Departure/arrival times changed (SSM: TIM).
 *  EQUIPMENT  – Aircraft type or configuration changed (SSM: EQT).
 *  CANCELLED  – Leg cancelled (SSM: CNL).
 *  REINSTATED – Previously cancelled leg restored (SSM: RIN).
 *  REVISED    – General field revision (SSM: REV).
 *  MERGED     – Result of merging an ingested SSIM full-schedule into live.
 * </pre>
 */
public enum ScheduleChangeType {

    NEW,
    RETIMED,
    EQUIPMENT,
    CANCELLED,
    REINSTATED,
    REVISED,
    MERGED
}
