package com.codeshare.airline.master.validation.service;

import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingDesignatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingModifierRepository;
import com.codeshare.airline.master.flight.passenger.repository.MealServiceRepository;
import com.codeshare.airline.master.flight.passenger.repository.SecureFlightIndicatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ServiceTypeRepository;
import com.codeshare.airline.master.flight.schedule.repository.FlightSuffixRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionQualifierRepository;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.repository.DeiRepository;
import com.codeshare.airline.master.messaging.repository.StandardMessageIdentifierRepository;
import com.codeshare.airline.master.schedule.repository.OperationalSuffixRepository;
import com.codeshare.airline.master.terminal.repository.PassengerTerminalRepository;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import com.codeshare.airline.platform.core.dto.master.validation.TerminalCodeDTO;
import com.codeshare.airline.platform.core.dto.master.validation.TrafficRestrictionQualifierCodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ScheduleCodeListValidationService {

    private final StandardMessageIdentifierRepository standardMessageIdentifierRepository;
    private final ActionIdentifierRepository actionIdentifierRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
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
    private final DeiRepository deiRepository;
    private final TrafficRestrictionCodeRepository trafficRestrictionCodeRepository;
    private final TrafficRestrictionQualifierRepository trafficRestrictionQualifierRepository;

    @Transactional(readOnly = true)
    public ScheduleCodeListValidationResponseDTO validate(ScheduleCodeListValidationRequestDTO request) {
        ScheduleCodeListValidationResponseDTO response = new ScheduleCodeListValidationResponseDTO();

        if (request == null) {
            response.setValid(true);
            return response;
        }

        validateOperatingAirline(response, request.getOperatingAirlineCode());
        validateCodes(response, "messageIdentifiers", request.getMessageIdentifiers(), "Unknown schedule message identifier",
                code -> standardMessageIdentifierRepository.existsByMessageIdentifier(normalize(code)));
        validateCodes(response, "actionCodes", request.getActionCodes(), "Unknown action identifier",
                code -> actionIdentifierRepository.existsByActionCode(normalize(code)));
        validateCodes(response, "airlineCodes", request.getAirlineCodes(), "Unknown airline designator",
                code -> airlineRepository.findByIataCode(normalize(code)).isPresent());
        validateCodes(response, "airportCodes", request.getAirportCodes(), "Unknown airport IATA code",
                code -> airportRepository.findByIataCode(normalize(code)).isPresent());
        validateCodes(response, "aircraftTypeCodes", request.getAircraftTypeCodes(), "Unknown IATA aircraft type",
                code -> aircraftTypeRepository.findByIataCode(normalize(code)).isPresent());
        validateAircraftConfigurations(response, request.getOperatingAirlineCode(), request.getAircraftConfigurationCodes());
        validateCodes(response, "serviceTypeCodes", request.getServiceTypeCodes(), "Unknown service type",
                code -> serviceTypeRepository.existsByServiceTypeCode(normalize(code)));
        validateCodes(response, "mealServiceCodes", request.getMealServiceCodes(), "Unknown meal service code",
                code -> mealServiceRepository.existsByMealCode(normalize(code)));
        validateCodes(response, "secureFlightIndicatorCodes", request.getSecureFlightIndicatorCodes(),
                "Unknown secure flight indicator",
                code -> secureFlightIndicatorRepository.existsBySecureFlightIndicatorCode(normalize(code)));
        validateCodes(response, "reservationBookingDesignators", request.getReservationBookingDesignators(),
                "Unknown reservation booking designator",
                code -> reservationBookingDesignatorRepository.existsByBookingDesignator(normalize(code)));
        validateCodes(response, "reservationBookingModifiers", request.getReservationBookingModifiers(),
                "Unknown reservation booking modifier",
                code -> reservationBookingModifierRepository.existsByModifierCode(normalize(code)));
        validateCodes(response, "operationalSuffixCodes", request.getOperationalSuffixCodes(),
                "Unknown operational suffix",
                code -> operationalSuffixRepository.findBySuffixCode(normalize(code)).isPresent());
        validateCodes(response, "flightSuffixCodes", request.getFlightSuffixCodes(), "Unknown flight suffix",
                code -> flightSuffixRepository.existsBySuffixCode(normalize(code)));
        validateTerminals(response, request.getTerminalCodes());
        validateCodes(response, "deiNumbers", request.getDeiNumbers(), "Unknown DEI number",
                code -> deiRepository.findByDeiNumber(normalizeDei(code)).isPresent());
        validateCodes(response, "trafficRestrictionCodes", request.getTrafficRestrictionCodes(),
                "Unknown traffic restriction code",
                code -> trafficRestrictionCodeRepository.existsByRestrictionCode(normalize(code)));
        validateTrafficRestrictionQualifiers(response, request.getTrafficRestrictionQualifiers());

        response.setValid(response.getErrors().isEmpty());
        return response;
    }

    private void validateTerminals(ScheduleCodeListValidationResponseDTO response, List<TerminalCodeDTO> terminals) {
        if (terminals == null) {
            return;
        }

        for (TerminalCodeDTO terminal : terminals) {
            if (terminal == null || isBlank(terminal.getAirportCode()) || isBlank(terminal.getTerminalCode())) {
                continue;
            }
            String airportCode = normalize(terminal.getAirportCode());
            String terminalCode = normalize(terminal.getTerminalCode());
            if (!passengerTerminalRepository.existsByAirport_IataCodeAndTerminalCode(airportCode, terminalCode)) {
                addError(response, "terminalCodes", airportCode + "/" + terminalCode, "Unknown passenger terminal");
            }
        }
    }

    private void validateOperatingAirline(ScheduleCodeListValidationResponseDTO response, String operatingAirlineCode) {
        if (!isBlank(operatingAirlineCode) && airline(operatingAirlineCode).isEmpty()) {
            addError(response, "operatingAirlineCode", normalize(operatingAirlineCode), "Unknown operating airline designator");
        }
    }

    private void validateAircraftConfigurations(ScheduleCodeListValidationResponseDTO response,
                                                String operatingAirlineCode,
                                                List<String> configurationCodes) {

        Optional<Airline> operatingAirline = airline(operatingAirlineCode);

        validateCodes(response, "aircraftConfigurationCodes", configurationCodes, "Unknown aircraft configuration",
                code -> {
                    String normalizedCode = normalize(code);
                    if (operatingAirline.isPresent()) {
                        return aircraftConfigurationRepository
                                .findByConfigurationCodeAndAirlineId(normalizedCode, operatingAirline.get().getId())
                                .isPresent();
                    }
                    return aircraftConfigurationRepository.findByConfigurationCode(normalizedCode).isPresent();
                });
    }

    private void validateTrafficRestrictionQualifiers(ScheduleCodeListValidationResponseDTO response,
                                                      List<TrafficRestrictionQualifierCodeDTO> qualifiers) {

        if (qualifiers == null) {
            return;
        }

        for (TrafficRestrictionQualifierCodeDTO qualifier : qualifiers) {
            if (qualifier == null || isBlank(qualifier.getRestrictionCode()) || isBlank(qualifier.getQualifierCode())) {
                continue;
            }
            String restrictionCode = normalize(qualifier.getRestrictionCode());
            String qualifierCode = normalize(qualifier.getQualifierCode());
            if (!trafficRestrictionQualifierRepository
                    .existsByTrafficRestrictionCode_RestrictionCodeAndQualifierCode(restrictionCode, qualifierCode)) {
                addError(response, "trafficRestrictionQualifiers", restrictionCode + "/" + qualifierCode,
                        "Unknown traffic restriction qualifier");
            }
        }
    }

    private Optional<Airline> airline(String airlineCode) {
        if (isBlank(airlineCode)) {
            return Optional.empty();
        }
        return airlineRepository.findByIataCode(normalize(airlineCode));
    }

    private void validateCodes(ScheduleCodeListValidationResponseDTO response,
                               String field,
                               List<String> codes,
                               String message,
                               Predicate<String> exists) {

        for (String code : distinct(codes)) {
            if (!exists.test(code)) {
                addError(response, field, code, message);
            }
        }
    }

    private Set<String> distinct(List<String> codes) {
        Set<String> values = new LinkedHashSet<>();
        if (codes == null) {
            return values;
        }
        for (String code : codes) {
            if (!isBlank(code)) {
                values.add(normalize(code));
            }
        }
        return values;
    }

    private void addError(ScheduleCodeListValidationResponseDTO response,
                          String field,
                          String code,
                          String message) {

        response.getErrors().add(new ScheduleCodeListValidationErrorDTO(field, code, message));
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }

    private String normalizeDei(String value) {
        String normalized = value.trim();
        if (normalized.matches("\\d{1,2}")) {
            return "0".repeat(3 - normalized.length()) + normalized;
        }
        return normalized;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
