package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftConfigurationRevision;

import java.util.List;

public interface AircraftConfigurationRevisionRepository
        extends CSMDataBaseRepository<AircraftConfigurationRevision, Long> {

    List<AircraftConfigurationRevision> findByAircraftConfigurationId(Long aircraftConfigurationId);
}
