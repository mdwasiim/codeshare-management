package com.codeshare.airline.master.airline.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.georegion.eitities.Airport;
import com.codeshare.airline.master.georegion.eitities.City;
import com.codeshare.airline.master.georegion.eitities.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRLINE_CARRIER",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "UK_AIRLINE_CARRIER_IATA",
                        columnNames = "IATA_CODE"
                ),

                @UniqueConstraint(
                        name = "UK_AIRLINE_CARRIER_ICAO",
                        columnNames = "ICAO_CODE"
                ),

                @UniqueConstraint(
                        name = "UK_AIRLINE_CARRIER_NUMERIC",
                        columnNames = "IATA_NUMERIC_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_AIRLINE_CARRIER_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_AIRLINE_CARRIER_COUNTRY",
                        columnList = "COUNTRY_ID"
                ),

                @Index(
                        name = "IDX_AIRLINE_CARRIER_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_AIRLINE_CARRIER_ICAO",
                        columnList = "ICAO_CODE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineCarrier extends CSMDataAbstractEntity {

    /**
     * IATA Two Letter Designator
     * Example:
     * QR
     * BA
     * EK
     */
    @Column(name = "IATA_CODE", nullable = false, length = 2)
    private String iataCode;

    /**
     * ICAO Three Letter Designator
     * Example:
     * QTR
     * BAW
     * UAE
     */
    @Column(name = "ICAO_CODE", nullable = false, length = 3)
    private String icaoCode;

    /**
     * IATA Numeric Code
     * Example:
     * 157
     * 125
     */
    @Column(name = "IATA_NUMERIC_CODE", length = 3)
    private String iataNumericCode;

    /**
     * Qatar Airways Company Q.C.S.C.
     */
    @Column(name = "LEGAL_NAME", nullable = false, length = 200)
    private String legalName;

    /**
     * Qatar Airways
     */
    @Column(name = "COMMERCIAL_NAME", nullable = false, length = 150)
    private String commercialName;

    /**
     * Display Name
     */
    @Column(name = "DISPLAY_NAME", nullable = false, length = 150)
    private String displayName;

    /**
     * QATAR
     * SPEEDBIRD
     */
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
            foreignKey = @ForeignKey(name = "FK_AIRLINE_CITY")
    )
    private City headquartersCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "HOME_AIRPORT_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_HOME_AIRPORT")
    )
    private Airport homeAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ALLIANCE_ID",
            foreignKey = @ForeignKey(name = "FK_AIRLINE_ALLIANCE")
    )
    private Alliance alliance;

    @Column(name = "WEBSITE", length = 250)
    private String website;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {

        if (iataCode == null || iataCode.isBlank()) {
            throw new IllegalStateException("IATA Designator is mandatory.");
        }

        if (icaoCode == null || icaoCode.isBlank()) {
            throw new IllegalStateException("ICAO Code is mandatory.");
        }

        if (legalName == null || legalName.isBlank()) {
            throw new IllegalStateException("Legal Name is mandatory.");
        }

        if (commercialName == null || commercialName.isBlank()) {
            throw new IllegalStateException("Commercial Name is mandatory.");
        }

        if (displayName == null || displayName.isBlank()) {
            throw new IllegalStateException("Display Name is mandatory.");
        }

        iataCode = iataCode.trim().toUpperCase();
        icaoCode = icaoCode.trim().toUpperCase();

        if (iataNumericCode != null) {
            iataNumericCode = iataNumericCode.trim();
        }

        legalName = legalName.trim();
        commercialName = commercialName.trim();
        displayName = displayName.trim();

        if (callsign != null) {
            callsign = callsign.trim().toUpperCase();
        }

        if (website != null) {
            website = website.trim();
        }

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (phone != null) {
            phone = phone.trim();
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}
