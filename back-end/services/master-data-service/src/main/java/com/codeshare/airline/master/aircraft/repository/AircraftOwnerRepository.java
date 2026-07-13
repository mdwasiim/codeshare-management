package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;

import java.util.Optional;

public interface AircraftOwnerRepository
        extends CSMDataBaseRepository<AircraftOwner, Long> {

    Optional<AircraftOwner> findByOwnerCode(String ownerCode);
}
