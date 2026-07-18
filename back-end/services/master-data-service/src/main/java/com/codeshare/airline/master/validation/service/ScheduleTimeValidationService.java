package com.codeshare.airline.master.validation.service;

import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.validation.util.ScheduleTimeValidationUtil;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationLegDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleTimeValidationService {

    private final AirportRepository airportRepository;
    private final ScheduleTimeValidationUtil util;

    @Transactional(readOnly = true)
    public ScheduleTimeValidationResponseDTO validate(ScheduleTimeValidationRequestDTO request) {
        ScheduleTimeValidationResponseDTO response = new ScheduleTimeValidationResponseDTO();
        if (request == null || request.getLegs() == null) {
            response.setValid(true);
            return response;
        }

        for (int i = 0; i < request.getLegs().size(); i++) {
            validateLeg(response, i, request.getLegs().get(i));
        }

        response.setValid(response.getErrors().isEmpty());
        return response;
    }

    private void validateLeg(ScheduleTimeValidationResponseDTO response, int index, ScheduleTimeValidationLegDTO leg) {
        if (leg == null) {
            return;
        }

        Optional<Airport> departureAirport = airport(leg.getDepartureAirport());
        Optional<Airport> arrivalAirport = airport(leg.getArrivalAirport());
        if (departureAirport.isEmpty()) {
            addError(response, index, "departureAirport", "Unknown departure airport: " + leg.getDepartureAirport());
            return;
        }
        if (arrivalAirport.isEmpty()) {
            addError(response, index, "arrivalAirport", "Unknown arrival airport: " + leg.getArrivalAirport());
            return;
        }
        if (leg.getDepartureDate() == null || leg.getDepartureTime() == null || leg.getArrivalTime() == null) {
            addError(response, index, "time", "Departure date, departure time and arrival time are required");
            return;
        }

        try {
            int departureOffset = util.parseOffsetMinutes(leg.getDepartureUtcOffset());
            int arrivalOffset = util.parseOffsetMinutes(leg.getArrivalUtcOffset());
            int dateVariation = util.parseDateVariation(leg.getDateVariation());
            LocalDate arrivalDate = leg.getArrivalDate() != null
                    ? leg.getArrivalDate()
                    : leg.getDepartureDate().plusDays(dateVariation);
            long actualDateVariation = util.dateVariation(leg.getDepartureDate(), arrivalDate);
            if (actualDateVariation != dateVariation) {
                addError(response, index, "arrivalDate",
                        "Arrival date does not match date variation " + dateVariation);
            }
            int expectedDepartureOffset = util.expectedOffsetMinutes(
                    departureAirport.get().getTimezone(),
                    leg.getDepartureDate(),
                    leg.getDepartureTime()
            );
            int expectedArrivalOffset = util.expectedOffsetMinutes(
                    arrivalAirport.get().getTimezone(),
                    arrivalDate,
                    leg.getArrivalTime()
            );

            if (departureOffset != expectedDepartureOffset) {
                addError(response, index, "departureUtcOffset",
                        "Expected " + util.formatOffset(expectedDepartureOffset) + " for " + leg.getDepartureAirport());
            }
            if (arrivalOffset != expectedArrivalOffset) {
                addError(response, index, "arrivalUtcOffset",
                        "Expected " + util.formatOffset(expectedArrivalOffset) + " for " + leg.getArrivalAirport());
            }

            Optional<Integer> expectedVariation = util.expectedDateVariation(
                    leg.getDepartureDate(),
                    leg.getDepartureTime(),
                    expectedDepartureOffset,
                    leg.getArrivalTime(),
                    expectedArrivalOffset
            );
            if (expectedVariation.isEmpty()) {
                addError(response, index, "dateVariation", "Unable to derive a reasonable arrival date variation");
            } else if (dateVariation != expectedVariation.get()) {
                addError(response, index, "dateVariation", "Expected " + expectedVariation.get());
            }
        } catch (RuntimeException ex) {
            addError(response, index, "time", ex.getMessage());
        }
    }

    private Optional<Airport> airport(String airportCode) {
        if (airportCode == null || airportCode.isBlank()) {
            return Optional.empty();
        }
        return airportRepository.findByIataCode(airportCode.trim().toUpperCase());
    }

    private void addError(ScheduleTimeValidationResponseDTO response, int index, String field, String message) {
        response.getErrors().add(new ScheduleTimeValidationErrorDTO("legs[" + index + "]." + field, message));
    }
}
