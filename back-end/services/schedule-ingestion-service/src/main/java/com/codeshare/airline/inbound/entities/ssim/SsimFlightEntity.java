package com.codeshare.airline.inbound.entities.ssim;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ssim_flight",
        indexes = {
                @Index(name = "idx_ssim_flight_carrier", columnList = "carrier_id"),
                @Index(name = "idx_ssim_flight_number", columnList = "airline_code,flight_number"),
                @Index(name = "idx_ssim_flight_route", columnList = "departure_station,arrival_station")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ssim_flight",
                        columnNames = {
                                "carrier_id",
                                "flight_number",
                                "operational_suffix",
                                "itinerary_variation_identifier"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SsimFlightEntity extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrier_id", nullable = false)
    private SsimCarrierEntity carrier;

    /* =======================================================
       SSIM RECORD TYPE 3 (T3) – 200 BYTES
       ======================================================= */

    // SSIM T3: Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType;

    // SSIM T3: Byte 2
    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;

    // SSIM T3: Bytes 3–5
    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    // SSIM T3: Bytes 6–9
    @Column(name = "flight_number", length = 5, nullable = false)
    private String flightNumber;

    // SSIM T3: Bytes 10–11
    @Column(name = "itinerary_variation_identifier", length = 2)
    private String itineraryVariationIdentifier;

    // SSIM T3: Bytes 12–13
    @Column(name = "leg_sequence_number", length = 2)
    private Integer legSequenceNumber;

    // SSIM T3: Byte 14
    @Column(name = "service_type", length = 1)
    private String serviceType;

    /* =======================================================
       15–35 PERIOD OF OPERATION
       ======================================================= */

    // SSIM T3: Bytes 15–21
    @Column(name = "operating_period_start_raw", length = 7)
    private String operatingPeriodStartRaw;

    // SSIM T3: Bytes 22–28
    @Column(name = "operating_period_end_raw", length = 7)
    private String operatingPeriodEndRaw;

    // SSIM T3: Bytes 29–35
    @Column(name = "operating_days", length = 7)
    private String operatingDays;

    /* =======================================================
       36 FREQUENCY
       ======================================================= */

    // SSIM T3: Byte 36
    @Column(name = "frequency_rate", length = 1)
    private String frequencyRate;

    /* =======================================================
       37–54 DEPARTURE
       ======================================================= */

    // SSIM T3: Bytes 37–39
    @Column(name = "departure_station", length = 3)
    private String departureStation;

    // SSIM T3: Bytes 40–43
    @Column(name = "passenger_std", length = 4)
    private String passengerStd;

    // SSIM T3: Bytes 44–47
    @Column(name = "aircraft_std", length = 4)
    private String aircraftStd;

    // SSIM T3: Bytes 48–52
    @Column(name = "departure_utc_variation", length = 5)
    private String departureUtcVariation;

    // SSIM T3: Bytes 53–54
    @Column(name = "departure_terminal", length = 2)
    private String departureTerminal;

    /* =======================================================
       55–72 ARRIVAL
       ======================================================= */

    // SSIM T3: Bytes 55–57
    @Column(name = "arrival_station", length = 3)
    private String arrivalStation;

    // SSIM T3: Bytes 58–61
    @Column(name = "aircraft_sta", length = 4)
    private String aircraftSta;

    // SSIM T3: Bytes 62–65
    @Column(name = "passenger_sta", length = 4)
    private String passengerSta;

    // SSIM T3: Bytes 66–70
    @Column(name = "arrival_utc_variation", length = 5)
    private String arrivalUtcVariation;

    // SSIM T3: Bytes 71–72
    @Column(name = "arrival_terminal", length = 2)
    private String arrivalTerminal;

    /* =======================================================
       73–110 AIRCRAFT & BOOKING
       ======================================================= */

    // SSIM T3: Bytes 73–75
    @Column(name = "aircraft_type", length = 3)
    private String aircraftType;

    // SSIM T3: Bytes 76–95
    @Column(name = "prbd", length = 20)
    private String passengerReservationBookingDesignator;

    // SSIM T3: Bytes 96–100
    @Column(name = "prbm", length = 5)
    private String passengerReservationBookingModifier;

    // SSIM T3: Bytes 101–110
    @Column(name = "meal_service_note", length = 10)
    private String mealServiceNote;

    /* =======================================================
       111–146 JOINT / ONWARD
       ======================================================= */

    // SSIM T3: Bytes 111–119
    @Column(name = "joint_operation_airline_designators", length = 9)
    private String jointOperationAirlineDesignators;

    // SSIM T3: Bytes 120–121
    @Column(name = "minimum_connecting_time_status", length = 2)
    private String minimumConnectingTimeStatus;

    // SSIM T3: Byte 122
    @Column(name = "secure_flight_indicator", length = 1)
    private String secureFlightIndicator;

    // SSIM T3: Bytes 123–127
    @Column(name = "spare_123_127", length = 5)
    private String spare123To127;

    // SSIM T3: Byte 128
    @Column(name = "itinerary_variation_overflow", length = 1)
    private String itineraryVariationOverflow;

    // SSIM T3: Bytes 129–131
    @Column(name = "aircraft_owner", length = 3)
    private String aircraftOwner;

    // SSIM T3: Bytes 132–134
    @Column(name = "cockpit_crew_employer", length = 3)
    private String cockpitCrewEmployer;

    // SSIM T3: Bytes 135–137
    @Column(name = "cabin_crew_employer", length = 3)
    private String cabinCrewEmployer;

    // SSIM T3: Bytes 138–140
    @Column(name = "onward_airline_designator", length = 3)
    private String onwardAirlineDesignator;

    // SSIM T3: Bytes 141–144
    @Column(name = "onward_flight_number", length = 4)
    private String onwardFlightNumber;

    // SSIM T3: Byte 145
    @Column(name = "aircraft_rotation_layover", length = 1)
    private String aircraftRotationLayover;

    // SSIM T3: Byte 146
    @Column(name = "onward_operational_suffix", length = 1)
    private String onwardOperationalSuffix;

    /* =======================================================
       147–172 DISCLOSURE / RESTRICTIONS
       ======================================================= */

    // SSIM T3: Byte 147
    @Column(name = "spare_147", length = 1)
    private String spare147;

    // SSIM T3: Byte 148
    @Column(name = "flight_transit_layover", length = 1)
    private String flightTransitLayover;

    // SSIM T3: Byte 149
    @Column(name = "operating_airline_disclosure", length = 1)
    private String operatingAirlineDisclosure;

    // SSIM T3: Bytes 150–160
    @Column(name = "traffic_restriction_code", length = 11)
    private String trafficRestrictionCode;

    // SSIM T3: Byte 161
    @Column(name = "traffic_restriction_overflow", length = 1)
    private String trafficRestrictionOverflow;

    // SSIM T3: Bytes 162–172
    @Column(name = "spare_162_172", length = 11)
    private String spare162To172;

    /* =======================================================
       173–194 CONFIGURATION
       ======================================================= */

    // SSIM T3: Bytes 173–192
    @Column(name = "aircraft_configuration_version", length = 20)
    private String aircraftConfigurationVersion;

    // SSIM T3: Bytes 193–194
    @Column(name = "date_variation", length = 2)
    private String dateVariation;

    /* =======================================================
       195–200 FOOTER
       ======================================================= */

    // SSIM T3: Bytes 195–200
    @Column(name = "record_serial_number", length = 6)
    private String recordSerialNumber;

    @OneToMany(
            mappedBy = "flight",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<SsimDataElementEntity> deis = new ArrayList<>();
}
