package com.codeshare.airline.data.core.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_REGION",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_REGION_CODE", columnNames = "REGION_CODE")
        },
        indexes = {
                @Index(name = "IDX_REGION_CODE", columnList = "REGION_CODE"),
                @Index(name = "IDX_REGION_STATUS", columnList = "STATUS_CODE")
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
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

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

