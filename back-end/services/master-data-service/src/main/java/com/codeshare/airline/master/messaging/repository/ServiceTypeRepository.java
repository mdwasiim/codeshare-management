package com.codeshare.airline.messaging.repository;

import com.codeshare.airline.messaging.eitities.ServiceType;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, UUID> {

    boolean existsByServiceTypeCode(String serviceTypeCode);

    Optional<ServiceType> findByServiceTypeCode(String serviceTypeCode);
}