package com.codeshare.airline.processor.entities.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ssim_inbound_r3_flight_leg",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ssim_r3_leg",
                columnNames = {
                        "dataset_id",
                        "airline_designator",
                        "flight_number",
                        "operational_suffix",
                        "origin_airport",
                        "destination_airport",
                        "period_start_date",
                        "period_end_date"
                }
        )
)

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR3FlightLegRecord extends CSMDataAbstractEntity {

    /* ===================== RELATIONSHIP ===================== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "dataset_id", nullable = false)
    private SsimInboundDatasetMetadata dataset;

    @Column(length = 1)
    private String itineraryVariation;

    @Column(length = 3)
    private String legSequenceNumber;

    /* ===================== SSIM FIELDS ===================== */

    // Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType; // '3'

    // Bytes 2–4
    @Column(name = "airline_designator", length = 3, nullable = false)
    private String airlineDesignator;

    // Bytes 5–8
    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    // Byte 9
    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;

    // Bytes 10–12
    @Column(name = "origin_airport", length = 3, nullable = false)
    private String originAirport;

    // Bytes 13–15
    @Column(name = "destination_airport", length = 3, nullable = false)
    private String destinationAirport;

    // Bytes 16–19
    @Column(name = "scheduled_departure_time", length = 4, nullable = false)
    private String scheduledDepartureTime;

    // Bytes 20–23
    @Column(name = "scheduled_arrival_time", length = 4, nullable = false)
    private String scheduledArrivalTime;

    // Byte 24
    @Column(name = "overnight_indicator", length = 1)
    private String overMidnightIndicator;

    // Bytes 25–30
    @Column(name = "period_start_date", length = 6, nullable = false)
    private String periodStartDate;

    // Bytes 31–36
    @Column(name = "period_end_date", length = 6, nullable = false)
    private String periodEndDate;

    // Bytes 37–43
    @Column(name = "days_of_operation", length = 7, nullable = false)
    private String daysOfOperation;

    // Bytes 44–46
    @Column(name = "aircraft_type", length = 3, nullable = false)
    private String aircraftType;

    // Bytes 47–66
    @Column(name = "aircraft_configuration", length = 20)
    private String aircraftConfiguration;

    // Bytes 67–68
    @Column(name = "service_type", length = 2, nullable = false)
    private String serviceType;

    // Bytes 69–70
    @Column(name = "traffic_restriction_code", length = 2)
    private String trafficRestrictionCode;

    // Byte 71 (reserved)
    @Column(name = "reserved_71", length = 1)
    private String reserved71;

    // Bytes 72–78
    @Column(name = "remarks", length = 7)
    private String remarks;

    /* ===================== CHILD RECORDS ===================== */

    @OneToMany(mappedBy = "flightLeg", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequence ASC")
    private List<SsimR3SegmentDataRecord> segmentData = new ArrayList<>();
}


