package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftConfigurationRepository
        extends CSMDataBaseRepository<AircraftConfiguration, UUID> {

    long countByAirlineIdAndAircraftTypeId(
            UUID airlineId,
            UUID aircraftTypeId
    );

    Optional<AircraftConfiguration> findByConfigurationCodeAndAirlineId(
            String configurationCode,
            UUID airlineId
    );

    Optional<AircraftConfiguration> findByConfigurationCode(String configurationCode);
}