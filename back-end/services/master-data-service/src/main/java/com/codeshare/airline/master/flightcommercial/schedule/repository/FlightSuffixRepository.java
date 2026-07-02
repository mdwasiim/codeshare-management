package com.codeshare.airline.master.flightcommercial.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flightcommercial.schedule.entities.FlightSuffix;

import java.util.UUID;

public interface FlightSuffixRepository extends CSMDataBaseRepository<FlightSuffix, UUID> {
}
