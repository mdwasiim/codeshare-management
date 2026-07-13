package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleProfile;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import org.hibernate.annotations.BatchSize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "schedule_file",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_schedule_file_id", columnNames = "file_id")
        },
        indexes = {
                @Index(name = "idx_schedule_status", columnList = "processing_status"),
                @Index(name = "idx_message_type", columnList = "message_type"),
                @Index(name = "idx_schedule_airline", columnList = "airline_code"),
                @Index(name = "idx_schedule_received", columnList = "received_at"),
                @Index(name = "idx_schedule_load", columnList = "load_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleFileMetaDataEntity extends CSMDataAbstractEntity {

    /* =======================================================
       IDENTIFIERS
       ======================================================= */

    @Column(name = "file_id", nullable = false, updatable = false)
    private UUID fileId;

    @Column(name = "load_id", nullable = false)
    private UUID loadId;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    /* =======================================================
       TYPE / SOURCE
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10, nullable = false)
    private MessageType messageType; // SSM / ASM

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 20)
    private SourceType sourceType;

    @Column(name = "airline_code", length = 3)
    private String airlineCode;

    /* =======================================================
       FILE DETAILS
       ======================================================= */

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum", length = 128)
    private String checksum;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    /* =======================================================
       PROCESSING STATE
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", length = 30, nullable = false)
    private ProcessingStatus processingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_profile", length = 20)
    private ScheduleProfile scheduleProfile;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "failed_at")
    private Instant failedTimestamp;

    @Column(name = "processed_at")
    private Instant processedAt;

    /* =======================================================
       VERSIONING
       ======================================================= */

    @Version
    @Column(name = "version")
    private Long version;

    /* =======================================================
       CHILDREN (SSM BLOCKS / ENVELOPES)
       ======================================================= */

    @OneToMany(
            mappedBy = "file",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @BatchSize(size = 100)
    private List<ScheduleMessageEntity> messageEnvelopes = new ArrayList<>();

    /* =======================================================
       HELPERS
       ======================================================= */

    public void addEnvelope(ScheduleMessageEntity envelope) {
        if (envelope != null) {
            messageEnvelopes.add(envelope);
            envelope.setFile(this);
        }
    }

    public List<ScheduleMessageEntity> getSafeEnvelopes() {
        return messageEnvelopes != null ? messageEnvelopes : new ArrayList<>();
    }
}
