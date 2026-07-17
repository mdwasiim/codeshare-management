package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleCodeListValidationRequestDTO {

    private String operatingAirlineCode;
    private List<String> messageIdentifiers = new ArrayList<>();
    private List<String> actionCodes = new ArrayList<>();
    private List<String> airlineCodes = new ArrayList<>();
    private List<String> airportCodes = new ArrayList<>();
    private List<String> aircraftTypeCodes = new ArrayList<>();
    private List<String> aircraftConfigurationCodes = new ArrayList<>();
    private List<String> serviceTypeCodes = new ArrayList<>();
    private List<String> mealServiceCodes = new ArrayList<>();
    private List<String> secureFlightIndicatorCodes = new ArrayList<>();
    private List<String> reservationBookingDesignators = new ArrayList<>();
    private List<String> reservationBookingModifiers = new ArrayList<>();
    private List<String> operationalSuffixCodes = new ArrayList<>();
    private List<String> flightSuffixCodes = new ArrayList<>();
    private List<TerminalCodeDTO> terminalCodes = new ArrayList<>();
    private List<String> deiNumbers = new ArrayList<>();
    private List<String> trafficRestrictionCodes = new ArrayList<>();
    private List<TrafficRestrictionQualifierCodeDTO> trafficRestrictionQualifiers = new ArrayList<>();
}
