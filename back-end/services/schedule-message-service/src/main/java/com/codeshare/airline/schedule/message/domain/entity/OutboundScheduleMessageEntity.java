package com.codeshare.airline.schedule.message.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbound_schedule_message",
        schema = "schedule_message",
        indexes = {
                @Index(name = "idx_osm_message_id", columnList = "outbound_message_id"),
                @Index(name = "idx_osm_change_set", columnList = "change_set_id"),
                @Index(name = "idx_osm_airline_partner", columnList = "airline_code,partner_code"),
                @Index(name = "idx_osm_message_type", columnList = "message_type"),
                @Index(name = "idx_osm_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_osm_message_id", columnNames = "outbound_message_id"),
                @UniqueConstraint(name = "uk_osm_change_set", columnNames = "change_set_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class OutboundScheduleMessageEntity extends CSMDataAbstractEntity {

    @Column(name = "outbound_message_id", nullable = false, updatable = false)
    private UUID outboundMessageId;

    @Column(name = "change_set_id", nullable = false, updatable = false)
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

    @Column(name = "payload", columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private OutboundScheduleMessageStatus status;

    @Column(name = "generated_at")
    private Instant generatedAt;

    @Column(name = "distribution_requested_at")
    private Instant distributionRequestedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
