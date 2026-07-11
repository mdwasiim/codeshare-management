package com.codeshare.airline.master.messaging.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Action_Code")
@Table(
        name = "ACTION_CODE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ACTION_CODE", columnNames = "ACTION_CODE")
        },
        indexes = {
                @Index(name = "IDX_ACTION_CODE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ActionCode extends CSMDataAbstractEntity {
    @Column(name = "ACTION_CODE", nullable = false, length = 3)
    private String actionCode;

    @Column(name = "ACTION_NAME", nullable = false, length = 150)
    private String actionName;


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
        if (actionCode != null) {
            actionCode = actionCode.trim().toUpperCase();
        }

        if (actionName != null) {
            actionName = actionName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ACTION_IDENTIFIER_ID",
            foreignKey = @ForeignKey(name = "FK_ACTION_CODE_ACTION_IDENTIFIER")
    )
    private ActionIdentifier actionIdentifier;
}