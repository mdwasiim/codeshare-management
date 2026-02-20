package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Entity
@Table(
        name = "SSIM_INBOUND_SEGMENT_DEI",
        schema = "SSIM_OPERATIONAL",
        indexes = {
                @Index(name = "IDX_SSIM_T4_FLIGHT", columnList = "FLIGHT_LEG_ID"),
                @Index(name = "IDX_SSIM_T4_ROUTE", columnList = "BOARD_POINT,OFF_POINT")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundSegmentDei extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FLIGHT_LEG_ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_SSIM_T4_FLIGHT_LEG")
    )
    private SsimInboundFlightLeg flightLeg;

    /* =======================================================
       1–14 HEADER
       ======================================================= */

    @Column(name = "RECORD_TYPE", length = 1)
    private String recordType;                       // 1

    @Column(name = "OPERATIONAL_SUFFIX", length = 1)
    private String operationalSuffix;                // 2

    @Column(name = "AIRLINE_CODE", length = 3)
    private String airlineCode;                      // 3–5

    @Column(name = "FLIGHT_NUMBER", length = 4)
    private String flightNumber;                     // 6–9

    @Column(name = "ITINERARY_VARIATION_IDENTIFIER", length = 2)
    private String itineraryVariationIdentifier;     // 10–11

    @Column(name = "LEG_SEQUENCE_NUMBER", length = 2)
    private String legSequenceNumber;                // 12–13

    @Column(name = "SERVICE_TYPE", length = 1)
    private String serviceType;                      // 14


    /* =======================================================
       15–28 STRUCTURE
       ======================================================= */

    @Column(name = "SPARE_15_27", length = 13)
    private String spare15To27;                      // 15–27

    @Column(name = "ITINERARY_VARIATION_OVERFLOW", length = 1)
    private String itineraryVariationOverflow;       // 28


    /* =======================================================
       29–39 SEGMENT IDENTIFIERS
       ======================================================= */

    @Column(name = "BOARD_POINT_INDICATOR", length = 1)
    private String boardPointIndicator;              // 29

    @Column(name = "OFF_POINT_INDICATOR", length = 1)
    private String offPointIndicator;                // 30

    @Column(name = "DATA_ELEMENT_IDENTIFIER", length = 3)
    private String dataElementIdentifier;            // 31–33

    @Column(name = "BOARD_POINT", length = 3)
    private String boardPoint;                       // 34–36

    @Column(name = "OFF_POINT", length = 3)
    private String offPoint;                         // 37–39


    /* =======================================================
       40–194 DATA PAYLOAD
       ======================================================= */

    @Column(name = "DEI_DATA", length = 155)
    private String deiData;                          // 40–194


    /* =======================================================
       195–200 FOOTER
       ======================================================= */

    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;               // 195–200


    /* =======================================================
       RAW & PROCESSING
       ======================================================= */

    @Column(name = "RAW_RECORD", length = 200)
    private String rawRecord;

    @Column(name = "PARSED_TIMESTAMP")
    private Instant parsedTimestamp;
}
