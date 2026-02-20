package com.codeshare.airline.data.ssim.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_ACTION_IDENTIFIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ACTION_IDENTIFIER_CODE", columnNames = "ACTION_CODE")
        },
        indexes = {
                @Index(name = "IDX_ACTION_IDENTIFIER_CODE", columnList = "ACTION_CODE"),
                @Index(name = "IDX_ACTION_IDENTIFIER_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ActionIdentifier extends CSMDataAbstractEntity {

    @Column(name = "ACTION_CODE", nullable = false, length = 10)
    private String actionCode;   // NEW, CNL, TIM, EQT...

    @Column(name = "ACTION_NAME", nullable = false, length = 100)
    private String actionName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "APPLICABLE_MESSAGE_TYPE", length = 20)
    private String applicableMessageType;   // ASM, SSM, BOTH

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (actionCode != null) {
            actionCode = actionCode.toUpperCase();
        }
        if (applicableMessageType != null) {
            applicableMessageType = applicableMessageType.toUpperCase();
        }
    }
}
