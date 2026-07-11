package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface AirlineFleetRepository
        extends CSMDataBaseRepository<AirlineFleetProfile, UUID> {

    List<AirlineFleetProfile> findByAirlineId(UUID airlineId);

    List<AirlineFleetProfile> findByAircraftConfigurationId(UUID configId);
}