package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.SecureFlightIndicator;


public interface SecureFlightIndicatorRepository extends CSMDataBaseRepository<SecureFlightIndicator, Long> {
    boolean existsBySecureFlightIndicatorCode(String secureFlightIndicatorCode);
}
