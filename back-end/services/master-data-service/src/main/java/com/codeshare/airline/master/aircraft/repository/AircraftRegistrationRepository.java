package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.aircraft.entities.AircraftRegistration;

import java.util.Optional;

public interface AircraftRegistrationRepository
        extends CSMDataBaseRepository<AircraftRegistration, Long> {

    Optional<AircraftRegistration> findByRegistrationNumber(String registrationNumber);
}
