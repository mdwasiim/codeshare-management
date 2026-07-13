package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface AircraftTypeRepository
        extends CSMDataBaseRepository<AircraftType, Long> {

    Optional<AircraftType> findByIcaoCode(String icaoCode);

    Optional<AircraftType> findByModel(String model);

    Optional<AircraftType> findByIataCode(String s);
}
