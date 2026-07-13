package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;

public interface AirlineFleetRepository
        extends CSMDataBaseRepository<AirlineFleetProfile, Long> {

    List<AirlineFleetProfile> findByAirlineId(Long airlineId);

    List<AirlineFleetProfile> findByAircraftConfigurationId(Long configId);
}