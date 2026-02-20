package com.codeshare.airline.ssim.inbound.persistence.control.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationSeverity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "SSIM_VALIDATION_REPORT",
        schema = "SSIM_CONTROL",
        indexes = {
                @Index(name = "IDX_SSIM_VAL_LOAD_ID", columnList = "LOAD_ID"),
                @Index(name = "IDX_SSIM_VAL_FILE_ID", columnList = "FILE_ID"),
                @Index(name = "IDX_SSIM_VAL_RECORD_TYPE", columnList = "RECORD_TYPE"),
                @Index(name = "IDX_SSIM_VAL_SEVERITY", columnList = "SEVERITY"),
                @Index(name = "IDX_SSIM_VAL_RULE_CODE", columnList = "RULE_CODE")
        }
)
@Getter
@Setter
public class ValidationReport extends CSMDataAbstractEntity {

    /* =======================================================
       CONTEXT
       ======================================================= */

    @Column(name = "LOAD_ID", nullable = false)
    private UUID loadId;

    @Column(name = "FILE_ID", length = 36, nullable = false)
    private String fileId;

    /**
     * T1, T2, T3, T4, T5
     */
    @Column(name = "RECORD_TYPE", length = 10, nullable = false)
    private String recordType;

    /**
     * Natural key representation:
     * flight number / carrier / leg sequence etc
     */
    @Column(name = "RECORD_KEY", length = 200)
    private String recordKey;


    /* =======================================================
       VALIDATION DETAILS
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "SEVERITY", length = 20, nullable = false)
    private ValidationSeverity severity;

    @Column(name = "RULE_CODE", length = 50, nullable = false)
    private String ruleCode;

    @Column(name = "MESSAGE", length = 1000, nullable = false)
    private String message;

    @Column(name = "BLOCKING", nullable = false)
    private boolean blocking;
}
