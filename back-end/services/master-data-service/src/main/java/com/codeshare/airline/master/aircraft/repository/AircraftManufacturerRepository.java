package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;

import java.util.Optional;

public interface AircraftManufacturerRepository
        extends CSMDataBaseRepository<AircraftManufacturer, Long> {

    Optional<AircraftManufacturer> findByManufacturerCode(String manufacturerCode);
}
