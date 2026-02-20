package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.ServiceType;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, UUID> {

    boolean existsByServiceTypeCode(String serviceTypeCode);

    Optional<ServiceType> findByServiceTypeCode(String serviceTypeCode);
}