package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;

import java.util.Optional;

public interface AircraftFamilyRepository
        extends CSMDataBaseRepository<AircraftFamily, Long> {

    Optional<AircraftFamily> findByFamilyCode(String familyCode);
}
