package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ElectronicTicketIndicator;


public interface ElectronicTicketIndicatorRepository extends CSMDataBaseRepository<ElectronicTicketIndicator, Long> {
    boolean existsByIndicatorCode(String indicatorCode);
}
