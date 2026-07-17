package com.codeshare.airline.master.flight.passenger.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;


public interface ServiceTypeRepository extends CSMDataBaseRepository<ServiceType, Long> {
    boolean existsByServiceTypeCode(String serviceTypeCode);
}
