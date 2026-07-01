package com.codeshare.airline.master.reference.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.georegion.eitities.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Holiday")
@Table(
        name = "HOLIDAY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_HOLIDAY", columnNames = "HOLIDAY_CODE")
        },
        indexes = {
                @Index(name = "IDX_HOLIDAY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Holiday extends CSMDataAbstractEntity {
    @Column(name = "HOLIDAY_CODE", nullable = false, length = 30)
    private String holidayCode;

    @Column(name = "HOLIDAY_NAME", nullable = false, length = 150)
    private String holidayName;

    @Column(name = "HOLIDAY_DATE")
    private LocalDate holidayDate;


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
        if (holidayCode != null) {
            holidayCode = holidayCode.trim().toUpperCase();
        }

        if (holidayName != null) {
            holidayName = holidayName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_HOLIDAY_COUNTRY")
    )
    private Country country;
}
