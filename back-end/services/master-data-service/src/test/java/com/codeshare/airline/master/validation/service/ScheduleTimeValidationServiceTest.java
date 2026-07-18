package com.codeshare.airline.master.validation.service;

import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.validation.util.ScheduleTimeValidationUtil;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationLegDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduleTimeValidationServiceTest {

    @Test
    void validateRejectsArrivalDateThatDoesNotMatchDateVariation() {
        AirportRepository airportRepository = mock(AirportRepository.class);
        ScheduleTimeValidationUtil util = mock(ScheduleTimeValidationUtil.class);
        ScheduleTimeValidationService service = new ScheduleTimeValidationService(airportRepository, util);

        Airport doh = airport("DOH", timezone(1L, 180));
        Airport lhr = airport("LHR", timezone(2L, 0));
        when(airportRepository.findByIataCode("DOH")).thenReturn(Optional.of(doh));
        when(airportRepository.findByIataCode("LHR")).thenReturn(Optional.of(lhr));
        when(util.parseOffsetMinutes("+0300")).thenReturn(180);
        when(util.parseOffsetMinutes("+0000")).thenReturn(0);
        when(util.parseDateVariation("0")).thenReturn(0);
        when(util.dateVariation(LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 19))).thenReturn(1L);
        when(util.expectedOffsetMinutes(doh.getTimezone(), LocalDate.of(2026, 7, 18), LocalTime.of(8, 0))).thenReturn(180);
        when(util.expectedOffsetMinutes(lhr.getTimezone(), LocalDate.of(2026, 7, 19), LocalTime.of(13, 0))).thenReturn(0);
        when(util.expectedDateVariation(LocalDate.of(2026, 7, 18), LocalTime.of(8, 0), 180, LocalTime.of(13, 0), 0))
                .thenReturn(Optional.of(0));

        ScheduleTimeValidationLegDTO leg = new ScheduleTimeValidationLegDTO();
        leg.setDepartureAirport("DOH");
        leg.setArrivalAirport("LHR");
        leg.setDepartureDate(LocalDate.of(2026, 7, 18));
        leg.setArrivalDate(LocalDate.of(2026, 7, 19));
        leg.setDepartureTime(LocalTime.of(8, 0));
        leg.setArrivalTime(LocalTime.of(13, 0));
        leg.setDateVariation("0");
        leg.setDepartureUtcOffset("+0300");
        leg.setArrivalUtcOffset("+0000");

        ScheduleTimeValidationRequestDTO request = new ScheduleTimeValidationRequestDTO();
        request.setLegs(List.of(leg));

        ScheduleTimeValidationResponseDTO response = service.validate(request);

        assertThat(response.isValid()).isFalse();
        assertThat(response.getErrors())
                .anySatisfy(error -> assertThat(error.getField()).isEqualTo("legs[0].arrivalDate"));
    }

    private Airport airport(String code, Timezone timezone) {
        Airport airport = new Airport();
        airport.setIataCode(code);
        airport.setTimezone(timezone);
        return airport;
    }

    private Timezone timezone(Long id, int offsetMinutes) {
        Timezone timezone = new Timezone();
        timezone.setId(id);
        timezone.setStandardUtcOffsetMinutes(offsetMinutes);
        return timezone;
    }
}
