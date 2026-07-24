package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "AIRPORT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_AIRPORT_IATA", columnNames = "IATA_CODE"),
                @UniqueConstraint(name = "UK_AIRPORT_ICAO", columnNames = "ICAO_CODE")
        },
        indexes = {
                @Index(name = "IDX_AIRPORT_CITY", columnList = "CITY_ID"),
                @Index(name = "IDX_AIRPORT_COUNTRY", columnList = "COUNTRY_ID"),
                @Index(name = "IDX_AIRPORT_REGION", columnList = "REGION_ID"),
                @Index(name = "IDX_AIRPORT_TIMEZONE", columnList = "TIMEZONE_ID"),
                @Index(name = "IDX_AIRPORT_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Airport extends CSMMasterDataEntity {

    @Column(name = "IATA_CODE", nullable = false, length = 3)
    private String iataCode;

    @Column(name = "ICAO_CODE", nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "IDENT", length = 20)
    private String ident;

    @Column(name = "AIRPORT_NAME", nullable = false, length = 200)
    private String airportName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "COUNTRY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_COUNTRY")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "REGION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_REGION")
    )
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "CITY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_CITY")
    )
    private City city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "TIMEZONE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRPORT_TIMEZONE")
    )
    private Timezone timezone;

    @Column(name = "AIRPORT_TYPE", length = 50)
    private String airportType;

    @Column(name = "LOCATION_TYPE", length = 50)
    private String locationType;

    @Column(name = "OPERATION_STATUS", length = 50)
    private String operationStatus;

    @Column(name = "OWNERSHIP_TYPE", length = 50)
    private String ownershipType;

    @Column(name = "SCHEDULED_SERVICE", length = 20)
    private String scheduledService;

    @Column(name = "GPS_CODE", length = 10)
    private String gpsCode;

    @Column(name = "LOCAL_CODE", length = 20)
    private String localCode;

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

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.trim().toUpperCase();
        }

        if (ident != null) {
            ident = ident.trim().toUpperCase();
        }

        if (airportName != null) {
            airportName = airportName.trim();
        }

        if (airportType != null) {
            airportType = airportType.trim().toUpperCase();
        }

        if (locationType != null) {
            locationType = locationType.trim().toUpperCase();
        }

        if (operationStatus != null) {
            operationStatus = operationStatus.trim().toUpperCase();
        }

        if (ownershipType != null) {
            ownershipType = ownershipType.trim().toUpperCase();
        }

        if (scheduledService != null) {
            scheduledService = scheduledService.trim().toUpperCase();
        }

        if (gpsCode != null) {
            gpsCode = gpsCode.trim().toUpperCase();
        }

        if (localCode != null) {
            localCode = localCode.trim().toUpperCase();
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

        if (city != null && country != null &&
                !city.getCountry().getId().equals(country.getId())) {
            throw new IllegalStateException("Airport country must match city's country.");
        }

        if (city != null && region != null &&
                !city.getRegion().getId().equals(region.getId())) {
            throw new IllegalStateException("Airport region must match city's region.");
        }
    }
}