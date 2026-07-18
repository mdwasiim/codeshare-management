package com.codeshare.airline.schedule.message.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageAuditEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbound_schedule_message_audit",
        schema = "schedule_message",
        indexes = {
                @Index(name = "idx_osm_audit_message_id", columnList = "outbound_message_id"),
                @Index(name = "idx_osm_audit_change_set", columnList = "change_set_id"),
                @Index(name = "idx_osm_audit_event_type", columnList = "event_type"),
                @Index(name = "idx_osm_audit_event_at", columnList = "event_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class OutboundScheduleMessageAuditEntity extends CSMDataAbstractEntity {

    @Column(name = "outbound_message_id", nullable = false)
    private UUID outboundMessageId;

    @Column(name = "change_set_id", nullable = false)
    private UUID changeSetId;

    @Column(name = "change_request_id")
    private Long changeRequestId;

    @Column(name = "imported_schedule_id")
    private UUID importedScheduleId;

    @Column(name = "import_batch_id")
    private UUID importBatchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10, nullable = false)
    private MessageType messageType;

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "partner_code", length = 10)
    private String partnerCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 40, nullable = false)
    private OutboundScheduleMessageAuditEventType eventType;

    @Column(name = "event_at", nullable = false)
    private Instant eventAt;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "causation_id")
    private UUID causationId;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
