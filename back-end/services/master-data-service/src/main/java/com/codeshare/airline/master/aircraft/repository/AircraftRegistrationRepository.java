package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftRegistration;

import java.util.Optional;
import java.util.UUID;

public interface AircraftRegistrationRepository
        extends CSMDataBaseRepository<AircraftRegistration, UUID> {

    Optional<AircraftRegistration> findByRegistrationNumber(String registrationNumber);
}
