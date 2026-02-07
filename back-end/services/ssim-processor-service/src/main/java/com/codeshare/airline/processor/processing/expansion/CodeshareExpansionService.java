package com.codeshare.airline.processor.processing.expansion;

import com.codeshare.airline.processor.processing.expansion.repository.FlightScheduleRepository;
import com.codeshare.airline.processor.model.domain.FlightSchedule;
import com.codeshare.airline.processor.model.raw.SsimAppendixHCodeshare;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import com.codeshare.airline.processor.processing.persistence.repository.SsimAppendixHCodeshareRepository;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeshareExpansionService {

    private final SsimAppendixHCodeshareRepository appendixHRepository;
    private final FlightScheduleRepository flightScheduleRepository;

    /**
     * Phase-2 Expansion:
     * Generates ALL marketing flight candidates.
     * Does NOT resolve conflicts.
     * Does NOT persist.
     */
    public List<FlightSchedule> expand(SsimLoadContext context) {

        List<FlightSchedule> marketingSchedules = new ArrayList<>();

        List<SsimAppendixHCodeshare> codeshares = appendixHRepository.findByOperatingLeg_SsimR1Header(context.getHeader());

        for (SsimAppendixHCodeshare cs : codeshares) {

            SsimR3FlightLegRecord leg = cs.getOperatingLeg();

            List<FlightSchedule> flightSchedules = flightScheduleRepository.findBySourceLegAndCodeshareFalse(leg);
            for (FlightSchedule op : flightSchedules) {

                FlightSchedule marketing =
                        FlightSchedule.builder()
                                .tenantId(op.getTenantId())
                                .scheduleVersion(op.getScheduleVersion())

                                .carrier(cs.getMarketingCarrier())
                                .flightNumber(cs.getMarketingFlightNumber())

                                .operatingCarrier(op.getCarrier())

                                .departureStation(op.getDepartureStation())
                                .arrivalStation(op.getArrivalStation())

                                .scheduledDepartureTime(op.getScheduledDepartureTime())
                                .scheduledArrivalTime(op.getScheduledArrivalTime())

                                .effectiveFrom(op.getEffectiveFrom())
                                .effectiveTo(op.getEffectiveTo())
                                .daysOfOperation(op.getDaysOfOperation())

                                .aircraftType(op.getAircraftType())
                                .serviceType(op.getServiceType())

                                .codeshare(true)
                                .build();

                marketingSchedules.add(marketing);
            }
        }

        return marketingSchedules;
    }
}
