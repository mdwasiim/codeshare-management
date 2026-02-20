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
        name = "MASTER_COUNTRY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_COUNTRY_ISO2", columnNames = "ISO2_CODE"),
                @UniqueConstraint(name = "UK_COUNTRY_ISO3", columnNames = "ISO3_CODE")
        },
        indexes = {
                @Index(name = "IDX_COUNTRY_REGION", columnList = "REGION_ID"),
                @Index(name = "IDX_COUNTRY_STATUS", columnList = "STATUS_CODE"),
                @Index(name = "IDX_COUNTRY_ISO2", columnList = "ISO2_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Country extends CSMDataAbstractEntity {

    @Column(name = "ISO2_CODE", nullable = false, length = 2)
    private String iso2Code;

    @Column(name = "ISO3_CODE", nullable = false, length = 3)
    private String iso3Code;

    @Column(name = "COUNTRY_NAME", nullable = false, length = 150)
    private String countryName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "REGION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_COUNTRY_REGION")
    )
    private Region region;

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
        if (iso2Code != null) {
            iso2Code = iso2Code.toUpperCase();
        }
        if (iso3Code != null) {
            iso3Code = iso3Code.toUpperCase();
        }
    }
}

