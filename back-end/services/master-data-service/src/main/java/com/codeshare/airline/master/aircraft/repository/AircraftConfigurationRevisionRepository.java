package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftConfigurationRevision;

import java.util.List;
import java.util.UUID;

public interface AircraftConfigurationRevisionRepository
        extends CSMDataBaseRepository<AircraftConfigurationRevision, UUID> {

    List<AircraftConfigurationRevision> findByAircraftConfigurationId(UUID aircraftConfigurationId);
}
