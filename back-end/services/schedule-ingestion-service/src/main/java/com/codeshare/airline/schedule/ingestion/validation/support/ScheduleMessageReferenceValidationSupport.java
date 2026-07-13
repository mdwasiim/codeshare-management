package com.codeshare.airline.schedule.ingestion.validation.support;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleLegDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleSubMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMessageReferenceValidationSupport {

    private final MasterDataReferenceValidationSupport masterDataValidation;

    public ScheduleMessageReferenceValidationSupport(MasterDataReferenceValidationSupport masterDataValidation) {
        this.masterDataValidation = masterDataValidation;
    }

    public ValidationResult validateScheduleMessage(ScheduleMessageDTO message, String prefix) {
        ValidationResult result = new ValidationResult();
        if (message == null || message.getMessages() == null) {
            return result;
        }

        for (ScheduleSubMessageDTO subMessage : message.getMessages()) {
            if (subMessage == null || subMessage.getFlights() == null) {
                continue;
            }

            for (ScheduleFlightDTO flight : subMessage.getFlights()) {
                if (flight == null) {
                    continue;
                }

                String recordKey = flightKey(flight);
                masterDataValidation.validateAirline(result, flight.getAirlineDesignator(), prefix + "_REF_001", "FLIGHT", recordKey);
                masterDataValidation.validateAircraft(result, flight.getAircraftType(), prefix + "_REF_002", "FLIGHT", recordKey);
                masterDataValidation.validateServiceType(result, flight.getServiceType(), prefix + "_REF_003", "FLIGHT", recordKey);

                if (flight.getLegs() != null) {
                    for (ScheduleLegDTO leg : flight.getLegs()) {
                        if (leg == null) {
                            continue;
                        }

                        String legKey = recordKey + "/" + leg.getLegSequenceNumber();
                        masterDataValidation.validateAirport(result, leg.getBoardPoint(), prefix + "_REF_010", "LEG", legKey);
                        masterDataValidation.validateAirport(result, leg.getOffPoint(), prefix + "_REF_011", "LEG", legKey);
                        masterDataValidation.validateAircraft(result, leg.getAircraftType(), prefix + "_REF_012", "LEG", legKey);
                        masterDataValidation.validateServiceType(result, leg.getServiceType(), prefix + "_REF_013", "LEG", legKey);

                        if (leg.getDeis() != null) {
                            for (ScheduleDataElementDTO dei : leg.getDeis()) {
                                validateSegmentDei(result, prefix, legKey, dei);
                            }
                        }
                    }
                }

                if (flight.getDeis() != null) {
                    for (ScheduleDataElementDTO dei : flight.getDeis()) {
                        validateSegmentDei(result, prefix, recordKey, dei);
                    }
                }
            }
        }

        return result;
    }

    private void validateSegmentDei(ValidationResult result, String prefix, String parentKey, ScheduleDataElementDTO dei) {
        if (dei == null) {
            return;
        }

        String deiKey = parentKey + "/DEI-" + dei.getDeiCode();
        masterDataValidation.validateAirport(result, dei.getBoardPoint(), prefix + "_REF_020", "DEI", deiKey);
        masterDataValidation.validateAirport(result, dei.getOffPoint(), prefix + "_REF_021", "DEI", deiKey);
    }

    private String flightKey(ScheduleFlightDTO flight) {
        return safe(flight.getAirlineDesignator())
                + safe(flight.getFlightNumber())
                + "/" + safe(flight.getOperationDate());
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
