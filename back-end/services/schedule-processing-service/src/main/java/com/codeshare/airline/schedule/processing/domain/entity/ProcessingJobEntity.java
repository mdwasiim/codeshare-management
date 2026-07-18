package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ProcessingJobStatus;
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
        name = "schedule_processing_job",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_spj_processing_job_id", columnList = "processing_job_id"),
                @Index(name = "idx_spj_import_batch", columnList = "import_batch_id"),
                @Index(name = "idx_spj_airline", columnList = "airline_code"),
                @Index(name = "idx_spj_partner", columnList = "partner_code"),
                @Index(name = "idx_spj_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_spj_processing_job_id", columnNames = "processing_job_id"),
                @UniqueConstraint(name = "uk_spj_import_batch_id", columnNames = "import_batch_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProcessingJobEntity extends CSMDataAbstractEntity {

    @Column(name = "processing_job_id", nullable = false, updatable = false)
    private UUID processingJobId;

    @Column(name = "imported_schedule_id", nullable = false, updatable = false)
    private UUID importedScheduleId;

    @Column(name = "import_batch_id", nullable = false, updatable = false)
    private UUID importBatchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10, nullable = false)
    private MessageType messageType;

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "partner_code", length = 10)
    private String partnerCode;

    @Column(name = "source_name", length = 255)
    private String sourceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private ProcessingJobStatus status;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
