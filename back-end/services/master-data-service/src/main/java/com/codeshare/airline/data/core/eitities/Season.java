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
        name = "MASTER_SEASON",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SEASON_CODE", columnNames = "SEASON_CODE")
        },
        indexes = {
                @Index(name = "IDX_SEASON_CODE", columnList = "SEASON_CODE"),
                @Index(name = "IDX_SEASON_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Season extends CSMDataAbstractEntity {

    @Column(name = "SEASON_CODE", nullable = false, length = 10)
    private String seasonCode;   // S25, W25

    @Column(name = "SEASON_NAME", nullable = false, length = 100)
    private String seasonName;   // Summer 2025

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private Status status;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (seasonCode != null) {
            seasonCode = seasonCode.toUpperCase();
        }
    }
}