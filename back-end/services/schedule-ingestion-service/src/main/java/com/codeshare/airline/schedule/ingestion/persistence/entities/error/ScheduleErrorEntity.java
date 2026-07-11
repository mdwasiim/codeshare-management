package com.codeshare.airline.schedule.ingestion.persistence.entities.error;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationSeverity;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "schedule_validation_report",
        indexes = {
                @Index(name = "idx_sch_val_load_id", columnList = "load_id"),
                @Index(name = "idx_sch_val_file_id", columnList = "file_id"),
                @Index(name = "idx_sch_val_severity", columnList = "severity"),
                @Index(name = "idx_sch_val_rule_code", columnList = "rule_code"), //  now valid
                @Index(name = "idx_sch_val_file_severity", columnList = "file_id,severity")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleErrorEntity extends CSMDataAbstractEntity {

    @Column(name = "load_id", nullable = false)
    private UUID loadId;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 10, nullable = false)
    private MessageType messageType;

    /**
     * T1, T2, T3, T4, T5
     */
    @Column(name = "record_type", length = 10)
    private String recordType;

    @Column(name = "record_key", length = 200)
    private String recordKey;

    //  CHANGED
    @Column(name = "rule_code", length = 50, nullable = false)
    private String ruleCode;

    //  CHANGED
    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 20, nullable = false)
    private ValidationSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_stage", length = 30)
    private ValidationStage validationStage;

    @Column(name = "validated_at", nullable = false)
    private Instant validatedAt = Instant.now();

    @Column(name = "rule_version", length = 20)
    private String ruleVersion;
}