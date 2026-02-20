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
        name = "MASTER_AIRLINE_CARRIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_AIRLINE_CARRIER_IATA", columnNames = "IATA_CODE"),
                @UniqueConstraint(name = "UK_AIRLINE_CARRIER_ICAO", columnNames = "ICAO_CODE")
        },
        indexes = {
                @Index(name = "IDX_AIRLINE_CARRIER_IATA", columnList = "IATA_CODE"),
                @Index(name = "IDX_AIRLINE_CARRIER_STATUS", columnList = "STATUS_CODE"),
                @Index(name = "IDX_AIRLINE_CARRIER_COUNTRY", columnList = "COUNTRY_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineCarrier extends CSMDataAbstractEntity {

    @Column(name = "LEGAL_NAME", nullable = false, length = 200)
    private String legalName;

    @Column(name = "COMMERCIAL_NAME", length = 200)
    private String commercialName;

    @Column(name = "IATA_CODE", nullable = false, length = 2)
    private String iataCode;

    @Column(name = "ICAO_CODE", nullable = false, length = 3)
    private String icaoCode;

    @Column(name = "CALLSIGN", length = 100)
    private String callsign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRLINE_CARRIER_COUNTRY")
    )
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (iataCode != null) {
            iataCode = iataCode.toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.toUpperCase();
        }

        if (iataCode == null || iataCode.length() != 2) {
            throw new IllegalStateException("IATA code must be 2 characters.");
        }

        if (icaoCode == null || icaoCode.length() != 3) {
            throw new IllegalStateException("ICAO code must be 3 characters.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

}

