package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;

import java.util.Optional;
import java.util.UUID;

public interface AircraftFamilyRepository
        extends CSMDataBaseRepository<AircraftFamily, UUID> {

    Optional<AircraftFamily> findByFamilyCode(String familyCode);
}
