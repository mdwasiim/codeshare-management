package com.codeshare.airline.inbound.entities.error;

import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationSeverity;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "ssim_validation_report",
        indexes = {
                @Index(name = "idx_ssim_val_load_id", columnList = "load_id"),
                @Index(name = "idx_ssim_val_file_id", columnList = "file_id"),
                @Index(name = "idx_ssim_val_record_type", columnList = "record_type"),
                @Index(name = "idx_ssim_val_severity", columnList = "severity"),
                @Index(name = "idx_ssim_val_rule_code", columnList = "rule_code"),
                @Index(name = "idx_ssim_val_file_severity", columnList = "file_id,severity")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimErrorEntity extends CSMDataAbstractEntity {

    /* =======================================================
       CONTEXT
       ======================================================= */

    @Column(name = "load_id", nullable = false)
    private UUID loadId;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    /**
     * T1, T2, T3, T4, T5
     */
    @Column(name = "record_type", length = 2, nullable = false)
    private String recordType;

    /**
     * Natural key (carrier+flight+leg etc.)
     */
    @Column(name = "record_key", length = 200)
    private String recordKey;

    /* =======================================================
       VALIDATION DETAILS
       ======================================================= */

    @Column(name = "rule_code", length = 50, nullable = false)
    private String ruleCode;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 20, nullable = false)
    private ValidationSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_stage", length = 30)
    private ValidationStage validationStage;

    /* =======================================================
       AUDIT
       ======================================================= */

    @Column(name = "validated_at", nullable = false)
    private Instant validatedAt = Instant.now();

    @Column(name = "rule_version", length = 20)
    private String ruleVersion;
}