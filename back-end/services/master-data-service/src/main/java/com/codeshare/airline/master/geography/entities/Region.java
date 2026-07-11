package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "REGION",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_REGION_CODE", columnNames = "REGION_CODE")
        },
        indexes = {
                @Index(name = "IDX_REGION_CODE", columnList = "REGION_CODE"),
                @Index(name = "IDX_REGION_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Region extends CSMDataAbstractEntity {

    @Column(name = "REGION_CODE", nullable = false, length = 10)
    private String regionCode;

    @Column(name = "REGION_NAME", nullable = false, length = 100)
    private String regionName;

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
        if (regionCode != null) {
            regionCode = regionCode.toUpperCase();
        }
    }
}

