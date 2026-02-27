package com.codeshare.airline.schedule.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFlight;

import java.time.LocalDate;
import java.util.UUID;

public interface ScheduleInboundFlightRepository
        extends CSMDataBaseRepository<ScheduleInboundFlight, UUID> {

    boolean existsByCarrierAndFlightNumberAndOperationDate(
            String carrier,
            String flightNumber,
            LocalDate operationDate
    );
}