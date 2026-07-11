package com.codeshare.airline.master.messaging.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Data_Element_Identifier")
@Table(
        name = "DATA_ELEMENT_IDENTIFIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_DATA_ELEMENT_IDENTIFIER", columnNames = "DEI_CODE")
        },
        indexes = {
                @Index(name = "IDX_DATA_ELEMENT_IDENTIFIER_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DataElementIdentifier extends CSMDataAbstractEntity {
    @Column(name = "DEI_CODE", nullable = false, length = 3)
    private String deiCode;

    @Column(name = "DEI_NAME", nullable = false, length = 150)
    private String deiName;

    @Column(name = "DEI_SCOPE", length = 30)
    private String deiScope;


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
        if (deiCode != null) {
            deiCode = deiCode.trim().toUpperCase();
        }

        if (deiName != null) {
            deiName = deiName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "STANDARD_MESSAGE_IDENTIFIER_ID",
            foreignKey = @ForeignKey(name = "FK_DATA_ELEMENT_IDENTIFIER_STANDARD_MESSAGE_IDENTIFIER")
    )
    private StandardMessageIdentifier standardMessageIdentifier;
}