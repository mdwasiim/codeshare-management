package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.FlightFrequency;


public interface FlightFrequencyRepository extends CSMDataBaseRepository<FlightFrequency, Long> {
    boolean existsByFrequencyCode(String frequencyCode);
}
