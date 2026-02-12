package com.codeshare.airline.ssim.persistence.inbound.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "SSIM_INBOUND_FLIGHT_LEG",
    schema = "SSIM_OPERATIONAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_SSIM_FLIGHT_LEG_NK",
            columnNames = {
                "FILE_ID",
                "OPERATIONAL_VARIATION",
                "AIRLINE_CODE",
                "FLIGHT_NUMBER",
                "OPERATIONAL_SUFFIX",
                "LEG_SEQUENCE"
            }
        )
    },
    indexes = {
        @Index(
            name = "IDX_SSIM_FLIGHT_LEG_FILE",
            columnList = "FILE_ID"
        ),
        @Index(
            name = "IDX_SSIM_FLIGHT_LEG_FLIGHT",
            columnList = "AIRLINE_CODE,FLIGHT_NUMBER"
        ),
        @Index(
            name = "IDX_SSIM_FLIGHT_LEG_ROUTE",
            columnList = "DEPARTURE_AIRPORT,ARRIVAL_AIRPORT"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundFlightLeg {

    /* ===================== IDENTITY ===================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLIGHT_LEG_ID")
    private Long id;

    /**
     * Owning inbound SSIM file
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", nullable = false, updatable = false)
    private SsimInboundFile file;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * Byte 1
     * Record type – always '3'
     */
    @Column(name = "RECORD_TYPE", length = 1, nullable = false)
    private String recordType;

    /**
     * Byte 2
     * Operational variation indicator
     */
    @Column(name = "OPERATIONAL_VARIATION", length = 1, nullable = false)
    private String operationalVariation;

    /**
     * Bytes 3–5
     */
    @Column(name = "AIRLINE_CODE", length = 3, nullable = false)
    private String airlineCode;

    /**
     * Bytes 6–9
     */
    @Column(name = "FLIGHT_NUMBER", length = 4, nullable = false)
    private String flightNumber;

    /**
     * Bytes 10–11
     */
    @Column(name = "OPERATIONAL_SUFFIX", length = 2, nullable = false)
    private String operationalSuffix;

    /**
     * Bytes 12–13
     */
    @Column(name = "LEG_SEQUENCE", length = 2, nullable = false)
    private String legSequence;

    /**
     * Byte 14
     */
    @Column(name = "SERVICE_TYPE", length = 1, nullable = false)
    private String serviceType;

    /* ===================== OPERATING PERIOD ===================== */

    /**
     * Bytes 15–21 (raw)
     */
    @Column(name = "PERIOD_START_RAW", length = 7, nullable = false)
    private String periodStartRaw;

    /**
     * Bytes 22–28 (raw)
     */
    @Column(name = "PERIOD_END_RAW", length = 7, nullable = false)
    private String periodEndRaw;

    /**
     * Parsed start date
     */
    @Column(name = "PERIOD_START_DATE")
    private LocalDate periodStartDate;

    /**
     * Parsed end date
     */
    @Column(name = "PERIOD_END_DATE")
    private LocalDate periodEndDate;

    /**
     * Bytes 29–35
     * Operating days bitmap (e.g. 1234567)
     */
    @Column(name = "OPERATING_DAYS", length = 7, nullable = false)
    private String operatingDays;

    /* ===================== DEPARTURE ===================== */

    @Column(name = "DEPARTURE_AIRPORT", length = 3, nullable = false)
    private String departureAirport;

    @Column(name = "DEPARTURE_TIME_RAW", length = 4, nullable = false)
    private String departureTimeRaw;

    @Column(name = "DEPARTURE_UTC_OFFSET_RAW", length = 5, nullable = false)
    private String departureUtcOffsetRaw;

    @Column(name = "DEPARTURE_DAY_RELATIVE", length = 1, nullable = false)
    private String departureDayRelative;

    @Column(name = "DEPARTURE_TERMINAL", length = 1)
    private String departureTerminal;

    /* ===================== ARRIVAL ===================== */

    @Column(name = "ARRIVAL_AIRPORT", length = 3, nullable = false)
    private String arrivalAirport;

    @Column(name = "ARRIVAL_TIME_RAW", length = 4, nullable = false)
    private String arrivalTimeRaw;

    @Column(name = "ARRIVAL_UTC_OFFSET_RAW", length = 5, nullable = false)
    private String arrivalUtcOffsetRaw;

    @Column(name = "ARRIVAL_DAY_CHANGE", length = 1, nullable = false)
    private String arrivalDayChange;

    @Column(name = "ARRIVAL_TERMINAL", length = 1)
    private String arrivalTerminal;

    /* ===================== EQUIPMENT / SERVICE ===================== */

    @Column(name = "AIRCRAFT_TYPE", length = 3, nullable = false)
    private String aircraftType;

    @Column(name = "BOOKING_CLASSES", length = 20)
    private String bookingClasses;

    @Column(name = "ONWARD_FLIGHT", length = 5)
    private String onwardFlight;

    @Column(name = "TRANSIT_INDICATOR", length = 1)
    private String transitIndicator;

    @Column(name = "MEAL_CODES", length = 35)
    private String mealCodes;

    /* ===================== DEI / EXTENSIONS ===================== */

    /**
     * Bytes 128–184 (raw DEI area if not split)
     */
    @Column(name = "DEI_RAW", length = 57)
    private String deiRaw;

    @Column(name = "SPARE_185_191", length = 7)
    private String spare185To191;

    @Column(name = "IVR_INDICATOR", length = 1)
    private String ivrIndicator;

    @Column(name = "DATA_SOURCE_CODE", length = 2)
    private String dataSourceCode;

    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    /* ===================== CHILD RECORDS ===================== */

    @OneToMany(
        mappedBy = "flightLeg",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<SsimInboundSegmentDei> segmentDeis = new ArrayList<>();

    /* ===================== RAW & AUDIT ===================== */

    @Column(name = "RAW_RECORD", length = 200, nullable = false)
    private String rawRecord;

    @Column(name = "PARSED_AT")
    private Instant parsedAt;
}
