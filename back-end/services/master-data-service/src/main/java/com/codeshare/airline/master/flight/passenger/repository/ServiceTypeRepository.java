package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;

import java.util.UUID;

public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, UUID> {
}
