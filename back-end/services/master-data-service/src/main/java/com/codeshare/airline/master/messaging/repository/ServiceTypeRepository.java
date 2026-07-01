package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.flightcommercial.passenger.entities.ServiceType;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, UUID> {

    boolean existsByServiceTypeCode(String serviceTypeCode);

    Optional<ServiceType> findByServiceTypeCode(String serviceTypeCode);
}