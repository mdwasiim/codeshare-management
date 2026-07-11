package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.SecureFlightIndicator;

import java.util.UUID;

public interface SecureFlightIndicatorRepository extends CSMDataBaseRepository<SecureFlightIndicator, UUID> {
    boolean existsBySecureFlightIndicatorCode(String secureFlightIndicatorCode);
}
