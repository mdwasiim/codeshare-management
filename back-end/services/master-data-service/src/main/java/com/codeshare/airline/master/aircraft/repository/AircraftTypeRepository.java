package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftTypeRepository
        extends CSMDataBaseRepository<AircraftType, UUID> {

    Optional<AircraftType> findByIcaoCode(String icaoCode);

    Optional<AircraftType> findByModel(String model);

    Optional<AircraftType> findByIataCode(String s);
}
