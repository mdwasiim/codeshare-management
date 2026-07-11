package com.codeshare.airline.master.messaging.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Reject_Reason")
@Table(
        name = "REJECT_REASON",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_REJECT_REASON", columnNames = "REJECT_REASON_CODE")
        },
        indexes = {
                @Index(name = "IDX_REJECT_REASON_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RejectReason extends CSMDataAbstractEntity {
    @Column(name = "REJECT_REASON_CODE", nullable = false, length = 10)
    private String rejectReasonCode;

    @Column(name = "REJECT_REASON_NAME", nullable = false, length = 150)
    private String rejectReasonName;


    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {
        if (rejectReasonCode != null) {
            rejectReasonCode = rejectReasonCode.trim().toUpperCase();
        }

        if (rejectReasonName != null) {
            rejectReasonName = rejectReasonName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}