package com.codeshare.airline.master.flightcommercial.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flightcommercial.schedule.entities.FlightFrequency;

import java.util.UUID;

public interface FlightFrequencyRepository extends CSMDataBaseRepository<FlightFrequency, UUID> {
}
