package com.codeshare.airline.processor.entities.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ssim_inbound_r4_date_variation")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR4DateVariationRecord extends CSMDataAbstractEntity {

    /* ===================== RELATIONSHIP ===================== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_leg_id", nullable = false)
    private SsimR3FlightLegRecord flightLeg;

    /* ===================== SSIM FIELDS ===================== */

    // Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType; // '5'

    // Bytes 10–15 (YYMMDD)
    @Column(name = "variation_date", length = 6, nullable = false)
    private String variationDate;

    // Byte 16
    @Column(name = "action_code", length = 1, nullable = false)
    private String actionCode;

    // Byte 17
    @Column(name = "day_change_indicator", length = 1)
    private String dayChangeIndicator;

    // Bytes 18–21
    @Column(name = "scheduled_departure_time", length = 4)
    private String scheduledDepartureTime;

    // Bytes 22–25
    @Column(name = "scheduled_arrival_time", length = 4)
    private String scheduledArrivalTime;

    // Bytes 26–28
    @Column(name = "aircraft_type", length = 3)
    private String aircraftType;

    // Bytes 29–30
    @Column(name = "traffic_restriction_code", length = 2)
    private String trafficRestrictionCode;

    // Byte 31 (reserved)
    @Column(name = "reserved_31", length = 1)
    private String reserved31;

    // Bytes 32–78
    @Column(name = "remarks", length = 47)
    private String remarks;

    /* ===================== CONTINUATION RECORDS ===================== */

    @OneToMany(mappedBy = "r4", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SsimR5ContinuationRecord> continuations = new ArrayList<>();
}

