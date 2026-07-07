package com.codeshare.airline.schedule.live.domain.enums;

/**
 * Delivery/processing status of an outbound SSM or ASM message
 * dispatched to the reservation system (Chapter 7).
 *
 * <pre>
 *  PENDING    – Generated but not yet dispatched.
 *  DISPATCHED – Sent to Kafka / reservation system topic.
 *  CONFIRMED  – Acknowledgement received from reservation system.
 *  FAILED     – Dispatch or processing error.
 *  RETRYING   – Scheduled for re-dispatch after failure.
 * </pre>
 */
public enum SsmOutboundStatus {

    PENDING,
    DISPATCHED,
    CONFIRMED,
    FAILED,
    RETRYING
}
