package com.codeshare.airline.master.messaging.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Standard_Message_Identifier")
@Table(
        name = "STANDARD_MESSAGE_IDENTIFIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_STANDARD_MESSAGE_IDENTIFIER", columnNames = "MESSAGE_IDENTIFIER")
        },
        indexes = {
                @Index(name = "IDX_STANDARD_MESSAGE_IDENTIFIER_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class StandardMessageIdentifier extends CSMDataAbstractEntity {
    @Column(name = "MESSAGE_IDENTIFIER", nullable = false, length = 3)
    private String messageIdentifier;

    @Column(name = "MESSAGE_IDENTIFIER_NAME", nullable = false, length = 150)
    private String messageIdentifierName;


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
        if (messageIdentifier != null) {
            messageIdentifier = messageIdentifier.trim().toUpperCase();
        }

        if (messageIdentifierName != null) {
            messageIdentifierName = messageIdentifierName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}