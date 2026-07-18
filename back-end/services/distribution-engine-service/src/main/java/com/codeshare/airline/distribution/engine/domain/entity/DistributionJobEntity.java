package com.codeshare.airline.distribution.engine.domain.entity;

import com.codeshare.airline.distribution.engine.domain.enums.DistributionJobStatus;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "distribution_job",
        schema = "distribution_engine",
        indexes = {
                @Index(name = "idx_dist_job_request", columnList = "distribution_request_id"),
                @Index(name = "idx_dist_job_change_set", columnList = "change_set_id"),
                @Index(name = "idx_dist_job_airline_partner", columnList = "airline_code,partner_code"),
                @Index(name = "idx_dist_job_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dist_job_request", columnNames = "distribution_request_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DistributionJobEntity extends CSMDataAbstractEntity {

    @Column(name = "distribution_request_id", nullable = false, updatable = false)
    private UUID distributionRequestId;

    @Column(name = "outbound_message_id", updatable = false)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private DistributionJobStatus status;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "dispatch_count")
    private Integer dispatchCount;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
