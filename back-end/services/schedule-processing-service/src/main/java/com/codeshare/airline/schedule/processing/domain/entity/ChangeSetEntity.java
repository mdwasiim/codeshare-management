package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ComparisonStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "schedule_change_set",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_scs_change_set_id", columnList = "change_set_id"),
                @Index(name = "idx_scs_airline", columnList = "airline_code"),
                @Index(name = "idx_scs_status", columnList = "status"),
                @Index(name = "idx_scs_source_type", columnList = "source_type"),
                @Index(name = "idx_scs_imported_schedule", columnList = "imported_schedule_id"),
                @Index(name = "idx_scs_import_batch", columnList = "import_batch_id"),
                @Index(name = "idx_scs_started_at", columnList = "started_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_scs_change_set_id", columnNames = "change_set_id"),
                @UniqueConstraint(name = "uk_scs_import_batch_id", columnNames = "import_batch_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ChangeSetEntity extends CSMDataAbstractEntity {

    @Column(name = "change_set_id", nullable = false, updatable = false)
    private UUID changeSetId;

    @Column(name = "imported_schedule_id", nullable = false, updatable = false)
    private UUID importedScheduleId;

    @Column(name = "import_batch_id", nullable = false, updatable = false)
    private UUID importBatchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 10, nullable = false)
    private MessageType sourceType;

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "message_reference", length = 50)
    private String messageReference;

    @Column(name = "source_system", length = 50)
    private String sourceSystem;

    @Column(name = "source_file_name", length = 255)
    private String sourceFileName;

    @Column(name = "source_checksum", length = 128)
    private String sourceChecksum;

    @Column(name = "source_creation_date_raw", length = 20)
    private String sourceCreationDateRaw;

    @Column(name = "source_creation_time_raw", length = 20)
    private String sourceCreationTimeRaw;

    @Column(name = "source_created_at")
    private Instant sourceCreatedAt;

    @Column(name = "source_received_at")
    private Instant sourceReceivedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ComparisonStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "total_legs_compared")
    private Integer totalLegsCompared;

    @Column(name = "new_count")
    private Integer newCount;

    @Column(name = "cancelled_count")
    private Integer cancelledCount;

    @Column(name = "retimed_count")
    private Integer retimedCount;

    @Column(name = "equipment_count")
    private Integer equipmentCount;

    @Column(name = "other_change_count")
    private Integer otherChangeCount;

    @Column(name = "no_change_count")
    private Integer noChangeCount;

    @OneToMany(
            mappedBy = "changeSet",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("airlineCode ASC, flightNumber ASC")
    @Builder.Default
    private List<ScheduleFlightChangeEntity> flightChanges = new ArrayList<>();

    public void addFlightChange(ScheduleFlightChangeEntity flightChange) {
        if (flightChange != null) {
            flightChanges.add(flightChange);
            flightChange.setChangeSet(this);
        }
    }
}
