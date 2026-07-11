package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.FlightFrequency;

import java.util.UUID;

public interface FlightFrequencyRepository extends CSMDataBaseRepository<FlightFrequency, UUID> {
    boolean existsByFrequencyCode(String frequencyCode);
}
