package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "SSIM_INBOUND_FLIGHT_LEG",
        schema = "SSIM_OPERATIONAL",
        indexes = {
                @Index(name = "IDX_SSIM_T3_FILE_ID", columnList = "FILE_ID"),
                @Index(name = "IDX_SSIM_T3_FLIGHT", columnList = "AIRLINE_CODE,FLIGHT_NUMBER"),
                @Index(name = "IDX_SSIM_T3_ROUTE", columnList = "DEPARTURE_STATION,ARRIVAL_STATION")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SsimInboundFlightLeg extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FILE_ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_SSIM_T3_FILE")
    )
    private SsimInboundFile inboundFile;

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
       15–35 PERIOD OF OPERATION
       ======================================================= */

    @Column(name = "OPERATING_PERIOD_START_RAW", length = 7)
    private String operatingPeriodStartRaw;          // 15–21

    @Column(name = "OPERATING_PERIOD_END_RAW", length = 7)
    private String operatingPeriodEndRaw;            // 22–28

    @Column(name = "OPERATING_DAYS", length = 7)
    private String operatingDays;                    // 29–35


    /* =======================================================
       36 FREQUENCY
       ======================================================= */

    @Column(name = "FREQUENCY_RATE", length = 1)
    private String frequencyRate;                    // 36


    /* =======================================================
       37–54 DEPARTURE
       ======================================================= */

    @Column(name = "DEPARTURE_STATION", length = 3)
    private String departureStation;                 // 37–39

    @Column(name = "PASSENGER_STD", length = 4)
    private String passengerStd;                     // 40–43

    @Column(name = "AIRCRAFT_STD", length = 4)
    private String aircraftStd;                      // 44–47

    @Column(name = "DEPARTURE_UTC_VARIATION", length = 5)
    private String departureUtcVariation;            // 48–52

    @Column(name = "DEPARTURE_TERMINAL", length = 2)
    private String departureTerminal;                // 53–54


    /* =======================================================
       55–72 ARRIVAL
       ======================================================= */

    @Column(name = "ARRIVAL_STATION", length = 3)
    private String arrivalStation;                   // 55–57

    @Column(name = "AIRCRAFT_STA", length = 4)
    private String aircraftSta;                      // 58–61

    @Column(name = "PASSENGER_STA", length = 4)
    private String passengerSta;                     // 62–65

    @Column(name = "ARRIVAL_UTC_VARIATION", length = 5)
    private String arrivalUtcVariation;              // 66–70

    @Column(name = "ARRIVAL_TERMINAL", length = 2)
    private String arrivalTerminal;                  // 71–72


    /* =======================================================
       73–110 AIRCRAFT & BOOKING
       ======================================================= */

    @Column(name = "AIRCRAFT_TYPE", length = 3)
    private String aircraftType;                     // 73–75

    @Column(name = "PRBD", length = 20)
    private String passengerReservationBookingDesignator;  // 76–95

    @Column(name = "PRBM", length = 5)
    private String passengerReservationBookingModifier;    // 96–100

    @Column(name = "MEAL_SERVICE_NOTE", length = 10)
    private String mealServiceNote;                  // 101–110


    /* =======================================================
       111–146 JOINT / ONWARD
       ======================================================= */

    @Column(name = "JOINT_OPERATION_AIRLINE_DESIGNATORS", length = 9)
    private String jointOperationAirlineDesignators; // 111–119

    @Column(name = "MINIMUM_CONNECTING_TIME_STATUS", length = 2)
    private String minimumConnectingTimeStatus;      // 120–121

    @Column(name = "SECURE_FLIGHT_INDICATOR", length = 1)
    private String secureFlightIndicator;            // 122

    @Column(name = "SPARE_123_127", length = 5)
    private String spare123To127;                    // 123–127

    @Column(name = "ITINERARY_VARIATION_OVERFLOW", length = 1)
    private String itineraryVariationOverflow;       // 128

    @Column(name = "AIRCRAFT_OWNER", length = 3)
    private String aircraftOwner;                    // 129–131

    @Column(name = "COCKPIT_CREW_EMPLOYER", length = 3)
    private String cockpitCrewEmployer;              // 132–134

    @Column(name = "CABIN_CREW_EMPLOYER", length = 3)
    private String cabinCrewEmployer;                // 135–137

    @Column(name = "ONWARD_AIRLINE_DESIGNATOR", length = 3)
    private String onwardAirlineDesignator;          // 138–140

    @Column(name = "ONWARD_FLIGHT_NUMBER", length = 4)
    private String onwardFlightNumber;               // 141–144

    @Column(name = "AIRCRAFT_ROTATION_LAYOVER", length = 1)
    private String aircraftRotationLayover;          // 145

    @Column(name = "ONWARD_OPERATIONAL_SUFFIX", length = 1)
    private String onwardOperationalSuffix;          // 146

    /* =======================================================
       147–172 DISCLOSURE / RESTRICTIONS
       ======================================================= */

    @Column(name = "SPARE_147", length = 1)
    private String spare147;                         // 147

    @Column(name = "FLIGHT_TRANSIT_LAYOVER", length = 1)
    private String flightTransitLayover;             // 148

    @Column(name = "OPERATING_AIRLINE_DISCLOSURE", length = 1)
    private String operatingAirlineDisclosure;       // 149

    @Column(name = "TRAFFIC_RESTRICTION_CODE", length = 11)
    private String trafficRestrictionCode;           // 150–160

    @Column(name = "TRAFFIC_RESTRICTION_OVERFLOW", length = 1)
    private String trafficRestrictionOverflow;       // 161

    @Column(name = "SPARE_162_172", length = 11)
    private String spare162To172;                    // 162–172


    /* =======================================================
       173–194 CONFIGURATION
       ======================================================= */

    @Column(name = "AIRCRAFT_CONFIGURATION_VERSION", length = 20)
    private String aircraftConfigurationVersion;     // 173–192

    @Column(name = "DATE_VARIATION", length = 2)
    private String dateVariation;                    // 193–194


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

    @OneToMany(
            mappedBy = "flightLeg",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SsimInboundSegmentDei> segmentDeis = new ArrayList<>();

}

