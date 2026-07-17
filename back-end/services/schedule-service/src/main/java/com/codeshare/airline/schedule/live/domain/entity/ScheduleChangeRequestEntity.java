package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalStatus;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
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
        name = "schedule_change_request",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_scrq_change_set", columnList = "change_set_id"),
                @Index(name = "idx_scrq_airline", columnList = "airline_code"),
                @Index(name = "idx_scrq_status", columnList = "approval_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_scrq_change_set_id", columnNames = "change_set_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleChangeRequestEntity extends CSMDataAbstractEntity {

    @Column(name = "change_set_id", nullable = false, updatable = false)
    private UUID changeSetId;

    @Column(name = "source_file_id")
    private UUID sourceFileId;

    @Column(name = "source_load_id")
    private UUID sourceLoadId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10, nullable = false)
    private MessageType messageType;

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_mode", length = 20, nullable = false)
    private ApprovalMode approvalMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 30, nullable = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "change_set_payload", columnDefinition = "TEXT", nullable = false)
    private String changeSetPayload;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "applied_at")
    private Instant appliedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
