package com.codeshare.airline.master.validation.service;

import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.flight.passenger.repository.MealServiceRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingDesignatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingModifierRepository;
import com.codeshare.airline.master.flight.passenger.repository.SecureFlightIndicatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ServiceTypeRepository;
import com.codeshare.airline.master.flight.schedule.repository.FlightSuffixRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionQualifierRepository;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.geography.repository.DstRuleRepository;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.repository.DeiRepository;
import com.codeshare.airline.master.messaging.repository.StandardMessageIdentifierRepository;
import com.codeshare.airline.master.schedule.repository.OperationalSuffixRepository;
import com.codeshare.airline.master.terminal.repository.PassengerTerminalRepository;
import com.codeshare.airline.platform.core.dto.master.validation.ReferenceDataCompletenessIssueDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ReferenceDataCompletenessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BooleanSupplier;

@Service
@RequiredArgsConstructor
public class ReferenceDataCompletenessService {

    private static final List<String> REQUIRED_MESSAGE_IDENTIFIERS = List.of("SSM", "ASM");
    private static final List<String> REQUIRED_ACTION_IDENTIFIERS = List.of("NEW", "CNL", "TIM", "EQT", "RIN", "REV", "COD", "FLT");
    private static final List<String> REQUIRED_DEI_NUMBERS = List.of("001", "002", "009", "101", "125", "127", "170", "210");

    private final StandardMessageIdentifierRepository standardMessageIdentifierRepository;
    private final ActionIdentifierRepository actionIdentifierRepository;
    private final DeiRepository deiRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final TimezoneRepository timezoneRepository;
    private final DstRuleRepository dstRuleRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final AircraftConfigurationRepository aircraftConfigurationRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final MealServiceRepository mealServiceRepository;
    private final SecureFlightIndicatorRepository secureFlightIndicatorRepository;
    private final ReservationBookingDesignatorRepository reservationBookingDesignatorRepository;
    private final ReservationBookingModifierRepository reservationBookingModifierRepository;
    private final OperationalSuffixRepository operationalSuffixRepository;
    private final FlightSuffixRepository flightSuffixRepository;
    private final PassengerTerminalRepository passengerTerminalRepository;
    private final TrafficRestrictionCodeRepository trafficRestrictionCodeRepository;
    private final TrafficRestrictionQualifierRepository trafficRestrictionQualifierRepository;

    @Transactional(readOnly = true)
    public ReferenceDataCompletenessResponseDTO outboundSchedule() {
        ReferenceDataCompletenessResponseDTO response = new ReferenceDataCompletenessResponseDTO();

        for (String code : REQUIRED_MESSAGE_IDENTIFIERS) {
            requiredCode(response, "standard-message-identifiers", code,
                    () -> standardMessageIdentifierRepository.existsByMessageIdentifier(code));
        }
        for (String code : REQUIRED_ACTION_IDENTIFIERS) {
            requiredCode(response, "action-identifiers", code,
                    () -> actionIdentifierRepository.existsByActionCode(code));
        }
        for (String code : REQUIRED_DEI_NUMBERS) {
            requiredCode(response, "dei", code,
                    () -> deiRepository.findByDeiNumber(code).isPresent());
        }

        requiredCategory(response, "airline-carriers", airlineRepository.count());
        requiredCategory(response, "airports", airportRepository.count());
        requiredCategory(response, "timezones", timezoneRepository.count());
        requiredCategory(response, "timezone-dst-periods", dstRuleRepository.count());
        requiredCategory(response, "aircraft-types", aircraftTypeRepository.count());
        requiredCategory(response, "aircraft-configurations", aircraftConfigurationRepository.count());
        requiredCategory(response, "service-types", serviceTypeRepository.count());
        requiredCategory(response, "meal-services", mealServiceRepository.count());
        requiredCategory(response, "secure-flight-indicators", secureFlightIndicatorRepository.count());
        requiredCategory(response, "reservation-booking-designators", reservationBookingDesignatorRepository.count());
        requiredCategory(response, "reservation-booking-modifiers", reservationBookingModifierRepository.count());
        requiredCategory(response, "operational-suffixes", operationalSuffixRepository.count());
        requiredCategory(response, "flight-suffixes", flightSuffixRepository.count());
        requiredCategory(response, "passenger-terminals", passengerTerminalRepository.count());
        requiredCategory(response, "traffic-restriction-codes", trafficRestrictionCodeRepository.count());
        requiredCategory(response, "traffic-restriction-qualifiers", trafficRestrictionQualifierRepository.count());

        response.setComplete(response.getIssues().isEmpty());
        return response;
    }

    private void requiredCode(ReferenceDataCompletenessResponseDTO response,
                              String category,
                              String code,
                              BooleanSupplier exists) {

        if (!exists.getAsBoolean()) {
            response.getIssues().add(new ReferenceDataCompletenessIssueDTO(category, code, "Required code is missing"));
        }
    }

    private void requiredCategory(ReferenceDataCompletenessResponseDTO response, String category, long count) {
        if (count == 0) {
            response.getIssues().add(new ReferenceDataCompletenessIssueDTO(category, null, "Reference category is empty"));
        }
    }
}
