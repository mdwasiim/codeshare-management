package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;

import java.util.Optional;
import java.util.UUID;

public interface AircraftOwnerRepository
        extends CSMDataBaseRepository<AircraftOwner, UUID> {

    Optional<AircraftOwner> findByOwnerCode(String ownerCode);
}
