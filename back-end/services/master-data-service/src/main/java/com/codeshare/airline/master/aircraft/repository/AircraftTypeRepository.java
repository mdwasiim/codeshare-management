package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.eitities.AircraftType;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftTypeRepository
        extends CSMDataBaseRepository<AircraftType, UUID> {

    Optional<AircraftType> findByIcaoCode(String icaoCode);

    Optional<AircraftType> findByModelCode(String modelCode);

    Optional<AircraftType> findByIataCode(String s);
}