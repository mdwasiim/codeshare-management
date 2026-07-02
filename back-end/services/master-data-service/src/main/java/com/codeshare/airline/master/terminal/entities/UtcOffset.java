package com.codeshare.airline.master.terminal.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.georegion.entities.Timezone;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Utc_Offset")
@Table(
        name = "UTC_OFFSET",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_UTC_OFFSET", columnNames = "OFFSET_CODE")
        },
        indexes = {
                @Index(name = "IDX_UTC_OFFSET_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class UtcOffset extends CSMDataAbstractEntity {
    @Column(name = "OFFSET_CODE", nullable = false, length = 10)
    private String offsetCode;

    @Column(name = "OFFSET_VALUE", nullable = false, length = 10)
    private String offsetValue;

    @Column(name = "OFFSET_MINUTES")
    private Integer offsetMinutes;


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
        if (offsetCode != null) {
            offsetCode = offsetCode.trim().toUpperCase();
        }

        if (offsetValue != null) {
            offsetValue = offsetValue.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TIMEZONE_ID",
            foreignKey = @ForeignKey(name = "FK_UTC_OFFSET_TIMEZONE")
    )
    private Timezone timezone;
}