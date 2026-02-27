package com.codeshare.airline.schedule.validation.persistence.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_VALIDATION",
        schema = "SCHEDULE_OPERATIONAL",
        indexes = {
                @Index(name = "IDX_VAL_FILE_ID", columnList = "FILE_ID"),
                @Index(name = "IDX_VAL_BLOCK_ID", columnList = "BLOCK_ID"),
                @Index(name = "IDX_VAL_FLIGHT_ID", columnList = "FLIGHT_ID"),
                @Index(name = "IDX_VAL_SEVERITY", columnList = "SEVERITY")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundValidation extends CSMDataAbstractEntity {

    // =============================
    // Ownership Context
    // =============================

    @Column(name = "FILE_ID", nullable = false)
    private UUID fileId;

    @Column(name = "BLOCK_ID")
    private UUID blockId; // Nullable for file-level validation

    @Column(name = "FLIGHT_ID")
    private UUID flightId; // Nullable for block/file level

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 10, nullable = false)
    private ScheduleMessageType messageType;

    // =============================
    // Validation Details
    // =============================

    @Column(name = "ERROR_CODE", length = 50, nullable = false)
    private String errorCode;

    @Column(name = "ERROR_MESSAGE", length = 1000, nullable = false)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEVERITY", length = 20, nullable = false)
    private ValidationSeverity severity;

    @Column(name = "BLOCKING", nullable = false)
    private Boolean blocking;

    @Column(name = "VALIDATION_STAGE", length = 30)
    private String validationStage;  // STRUCTURAL / BUSINESS

    @Column(name = "VALIDATED_AT", nullable = false)
    private Instant validatedAt;

    // Optional future-proofing
    @Column(name = "RULE_VERSION", length = 20)
    private String ruleVersion;
}