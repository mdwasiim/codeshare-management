package com.codeshare.airline.inbound.dto.common.ssim;

import com.codeshare.airline.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.inbound.domain.enums.RecordType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SsimFlightDTO extends CSMAuditableDTO implements SSIMMessageItem {

    /* =======================================================
       1–14 HEADER
       ======================================================= */

    private RecordType recordType;                     // Byte 1
    private String operationalSuffix;              // Byte 2
    private String airlineCode;                    // Bytes 3–5
    private String flightNumber;                   // Bytes 6–9
    private String itineraryVariationIdentifier;   // Bytes 10–11
    private Integer legSequenceNumber;              // Bytes 12–13
    private String serviceType;                    // Byte 14

    /* =======================================================
       15–35 PERIOD OF OPERATION
       ======================================================= */

    private String operatingPeriodStartRaw;        // Bytes 15–21
    private String operatingPeriodEndRaw;          // Bytes 22–28
    private String operatingDays;                  // Bytes 29–35

    /* =======================================================
       36 FREQUENCY
       ======================================================= */

    private String frequencyRate;                  // Byte 36

    /* =======================================================
       37–54 DEPARTURE
       ======================================================= */

    private String departureStation;               // Bytes 37–39
    private String passengerStd;                   // Bytes 40–43
    private String aircraftStd;                    // Bytes 44–47
    private String departureUtcVariation;          // Bytes 48–52
    private String departureTerminal;              // Bytes 53–54

    /* =======================================================
       55–72 ARRIVAL
       ======================================================= */

    private String arrivalStation;                 // Bytes 55–57
    private String aircraftSta;                    // Bytes 58–61
    private String passengerSta;                   // Bytes 62–65
    private String arrivalUtcVariation;            // Bytes 66–70
    private String arrivalTerminal;                // Bytes 71–72

    /* =======================================================
       73–110 AIRCRAFT & BOOKING
       ======================================================= */

    private String aircraftType;                   // Bytes 73–75
    private String passengerReservationBookingDesignator;  // Bytes 76–95
    private String passengerReservationBookingModifier;    // Bytes 96–100
    private String mealServiceNote;                // Bytes 101–110

    /* =======================================================
       111–146 JOINT / ONWARD
       ======================================================= */

    private String jointOperationAirlineDesignators; // Bytes 111–119
    private String minimumConnectingTimeStatus;      // Bytes 120–121
    private String secureFlightIndicator;            // Byte 122
    private String spare123To127;                    // Bytes 123–127
    private String itineraryVariationOverflow;       // Byte 128
    private String aircraftOwner;                    // Bytes 129–131
    private String cockpitCrewEmployer;              // Bytes 132–134
    private String cabinCrewEmployer;                // Bytes 135–137
    private String onwardAirlineDesignator;          // Bytes 138–140
    private String onwardFlightNumber;               // Bytes 141–144
    private String aircraftRotationLayover;          // Byte 145
    private String onwardOperationalSuffix;          // Byte 146

    /* =======================================================
       147–172 DISCLOSURE / RESTRICTIONS
       ======================================================= */

    private String spare147;                        // Byte 147
    private String flightTransitLayover;            // Byte 148
    private String operatingAirlineDisclosure;      // Byte 149
    private String trafficRestrictionCode;          // Bytes 150–160
    private String trafficRestrictionOverflow;      // Byte 161
    private String spare162To172;                   // Bytes 162–172

    /* =======================================================
       173–194 CONFIGURATION
       ======================================================= */

    private String aircraftConfigurationVersion;    // Bytes 173–192
    private String dateVariation;                   // Bytes 193–194

    /* =======================================================
       195–200 FOOTER
       ======================================================= */

    private String recordSerialNumber;              // Bytes 195–200

    private List<SsimDataElementDTO> deis = new ArrayList<>();
}