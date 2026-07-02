package com.codeshare.airline.master.messaging.entities;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "ACTION_IDENTIFIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ACTION_IDENTIFIER_CODE", columnNames = "ACTION_CODE")
        },
        indexes = {
                @Index(name = "IDX_ACTION_IDENTIFIER_CODE", columnList = "ACTION_CODE"),
                @Index(name = "IDX_ACTION_IDENTIFIER_STATUS", columnList = "STATUS")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 20)
    private MessageType messageType;   // ASM, SSM, BOTH

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

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
    }
}
