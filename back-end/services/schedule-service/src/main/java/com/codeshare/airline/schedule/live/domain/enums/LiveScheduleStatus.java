package com.codeshare.airline.schedule.live.domain.enums;

/**
 * Lifecycle status of a live flight leg record in the canonical schedule store.
 *
 * <pre>
 *  ACTIVE      – Current, authoritative leg visible to downstream systems.
 *  SUPERSEDED  – Replaced by a newer version via SSM/SSIM update; kept for history.
 *  CANCELLED   – Flight leg cancelled via CNL action type (Chapter 7 SSM).
 *  WITHDRAWN   – Administratively removed; not published to reservation system.
 * </pre>
 */
public enum LiveScheduleStatus {

    ACTIVE,
    SUPERSEDED,
    CANCELLED,
    WITHDRAWN
}
