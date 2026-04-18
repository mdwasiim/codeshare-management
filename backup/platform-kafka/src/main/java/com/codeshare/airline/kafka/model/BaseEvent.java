package com.codeshare.airline.kafka.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {

    private String eventId = UUID.randomUUID().toString();
    private Instant timestamp = Instant.now();

    /** Who produced this event */
    private String source;

    /** Distributed tracing */
    private String traceId;

    /** For multi-tenant systems */
    private String tenantId;

    /** Schema / contract evolution */
    private int version = 1;

    /** Idempotency support */
    private String idempotencyKey;
}
