package com.codeshare.airline.data.core.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_AIRPORT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_AIRPORT_IATA", columnNames = "IATA_CODE"),
                @UniqueConstraint(name = "UK_AIRPORT_ICAO", columnNames = "ICAO_CODE")
        },
        indexes = {
                @Index(name = "IDX_AIRPORT_CITY", columnList = "CITY_ID"),
                @Index(name = "IDX_AIRPORT_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_AIRPORT_TIMEZONE", columnList = "TIMEZONE_ID"),
                @Index(name = "IDX_AIRPORT_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Airport extends CSMDataAbstractEntity {

    @Column(name = "IATA_CODE", nullable = false, length = 3)
    private String iataCode;

    @Column(name = "ICAO_CODE", nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "AIRPORT_NAME", nullable = false, length = 200)
    private String airportName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "CITY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_CITY")
    )
    private City city;

    // Keep for performance (denormalized reference)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_COUNTRY")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "TIMEZONE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_TIMEZONE")
    )
    private Timezone timezone;

    @Column(name = "LATITUDE", precision = 10, scale = 7, nullable = false)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 10, scale = 7, nullable = false)
    private BigDecimal longitude;

    @Column(name = "ELEVATION_FEET")
    private Integer elevationFeet;

    @Column(name = "IS_INTERNATIONAL", nullable = false)
    private Boolean international = Boolean.FALSE;

    @Column(name = "IS_HUB", nullable = false)
    private Boolean hub = Boolean.FALSE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.trim().toUpperCase();
        }

        if (latitude != null &&
                (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 ||
                        latitude.compareTo(BigDecimal.valueOf(90)) > 0)) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }

        if (longitude != null &&
                (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 ||
                        longitude.compareTo(BigDecimal.valueOf(180)) > 0)) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("EffectiveFrom must be before EffectiveTo.");
        }

        // Data consistency check
        if (city != null && country != null &&
                !city.getCountry().getId().equals(country.getId())) {
            throw new IllegalStateException(
                    "Airport country must match city's country."
            );
        }
    }
}