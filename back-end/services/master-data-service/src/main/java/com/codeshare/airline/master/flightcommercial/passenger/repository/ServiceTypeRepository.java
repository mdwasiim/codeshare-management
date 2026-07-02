package com.codeshare.airline.master.flightcommercial.passenger.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flightcommercial.passenger.entities.ServiceType;

import java.util.UUID;

public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, UUID> {
}
