package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface AircraftConfigurationRepository
        extends CSMDataBaseRepository<AircraftConfiguration, Long> {

    long countByAirlineIdAndAircraftTypeId(
            Long airlineId,
            Long aircraftTypeId
    );

    Optional<AircraftConfiguration> findByConfigurationCodeAndAirlineId(
            String configurationCode,
            Long airlineId
    );

    Optional<AircraftConfiguration> findByConfigurationCode(String configurationCode);
}