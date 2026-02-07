package com.codeshare.airline.processor.processing.expansion;

import com.codeshare.airline.processor.processing.expansion.repository.FlightScheduleRepository;
import com.codeshare.airline.processor.model.domain.FlightSchedule;
import com.codeshare.airline.processor.model.domain.ScheduleVersion;
import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightScheduleExpanderImpl implements FlightScheduleExpander {

    private final FlightScheduleRepository flightScheduleRepository;

    @Override
    public void expand(SsimLoadContext context) {

        ScheduleVersion scheduleVersion = context.getScheduleVersion();
        SsimR1HeaderRecord header = context.getHeader();

        for (SsimR3FlightLegRecord leg : context.getFlightLegs()) {

            LocalDate date = leg.getEffectiveFrom();

            while (!date.isAfter(leg.getEffectiveTo())) {

                if (operatesOn(leg.getDaysOfOperation(), date)) {

                    FlightSchedule schedule =
                            FlightSchedule.builder()

                                    /* tenant & version */
                                    .tenantId(header.getTenantId())
                                    .scheduleVersion(scheduleVersion)

                                    /* flight identity */
                                    .carrier(leg.getAirlineDesignator())
                                    .flightNumber(leg.getFlightNumber())
                                    .operatingCarrier(leg.getOperatingCarrier())

                                    /* route */
                                    .departureStation(leg.getDepartureStation())
                                    .arrivalStation(leg.getArrivalStation())

                                    /* times */
                                    .scheduledDepartureTime(
                                            leg.getScheduledDepartureTime())
                                    .scheduledArrivalTime(
                                            leg.getScheduledArrivalTime())

                                    /* DATE (this is the key change) */
                                    .operationDate(date)

                                    /* service */
                                    .aircraftType(leg.getAircraftType())
                                    .serviceType(leg.getServiceType())

                                    /* flags */
                                    .codeshare(false)

                                    .build();

                    flightScheduleRepository.save(schedule);
                }

                date = date.plusDays(1);
            }
        }
    }

    private boolean operatesOn(String days, LocalDate date) {
        int index = date.getDayOfWeek().getValue() % 7; // Sun=0
        return days.charAt(index) == '1';
    }
}

