package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.FlightSuffix;


public interface FlightSuffixRepository extends CSMDataBaseRepository<FlightSuffix, Long> {
    boolean existsBySuffixCode(String suffixCode);
}
