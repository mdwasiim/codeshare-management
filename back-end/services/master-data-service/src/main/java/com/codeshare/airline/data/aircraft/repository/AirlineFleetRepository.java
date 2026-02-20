package com.codeshare.airline.data.aircraft.repository;

import com.codeshare.airline.data.aircraft.eitities.AirlineFleet;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface AirlineFleetRepository
        extends CSMDataBaseRepository<AirlineFleet, UUID> {

    List<AirlineFleet> findByAirlineId(UUID airlineId);

    List<AirlineFleet> findByAircraftConfigurationId(UUID configId);
}