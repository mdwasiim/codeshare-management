package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ElectronicTicketIndicator;

import java.util.UUID;

public interface ElectronicTicketIndicatorRepository extends CSMDataBaseRepository<ElectronicTicketIndicator, UUID> {
}
