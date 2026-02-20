package com.codeshare.airline.data.aircraft.repository;

import com.codeshare.airline.data.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

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