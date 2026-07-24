package com.codeshare.airline.master.airlines.entities;

import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRLINE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_IATA",
                        columnNames = "IATA_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRLINE_ICAO",
                        columnNames = "ICAO_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRLINE_NUMERIC",
                        columnNames = "IATA_NUMERIC_CODE"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRLINE_COUNTRY",
                        columnList = "COUNTRY_ID"
                ),
                @Index(
                        name = "IDX_AIRLINE_HOME_AIRPORT",
                        columnList = "HOME_AIRPORT_ID"
                ),
                @Index(
                        name = "IDX_AIRLINE_ACTIVE",
                        columnList = "ACTIVE"
                ),
                @Index(
                        name = "IDX_AIRLINE_DISPLAY_ORDER",
                        columnList = "DISPLAY_ORDER"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Airline extends CSMMasterDataEntity {

    @Column(name = "IATA_CODE", nullable = false, length = 2)
    private String iataCode;

    @Column(name = "ICAO_CODE", nullable = false, length = 3)
    private String icaoCode;

    @Column(name = "IATA_NUMERIC_CODE", length = 3)
    private String iataNumericCode;

    @Column(name = "LEGAL_NAME", nullable = false, length = 200)
    private String legalName;

    @Column(name = "COMMERCIAL_NAME", nullable = false, length = 150)
    private String commercialName;

    @Column(name = "DISPLAY_NAME", nullable = false, length = 150)
    private String displayName;

    @Column(name = "CALLSIGN", length = 50)
    private String callsign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_COUNTRY")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "HEADQUARTERS_CITY_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_HEADQUARTERS_CITY")
    )
    private City headquartersCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "HOME_AIRPORT_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_HOME_AIRPORT")
    )
    private Airport homeAirport;

    @Column(name = "WEBSITE", length = 250)
    private String website;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {

        if (iataCode == null || iataCode.isBlank())
            throw new IllegalStateException("IATA Code is mandatory.");

        if (icaoCode == null || icaoCode.isBlank())
            throw new IllegalStateException("ICAO Code is mandatory.");

        if (legalName == null || legalName.isBlank())
            throw new IllegalStateException("Legal Name is mandatory.");

        if (commercialName == null || commercialName.isBlank())
            throw new IllegalStateException("Commercial Name is mandatory.");

        if (displayName == null || displayName.isBlank())
            throw new IllegalStateException("Display Name is mandatory.");

        iataCode = iataCode.trim().toUpperCase();
        icaoCode = icaoCode.trim().toUpperCase();

        if (iataNumericCode != null)
            iataNumericCode = iataNumericCode.trim();

        legalName = legalName.trim();
        commercialName = commercialName.trim();
        displayName = displayName.trim();

        if (callsign != null)
            callsign = callsign.trim().toUpperCase();

        if (website != null)
            website = website.trim();
    }
}