package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;


public interface TrafficRestrictionCodeRepository extends CSMDataBaseRepository<TrafficRestrictionCode, Long> {
    boolean existsByRestrictionCode(String restrictionCode);

    java.util.Optional<TrafficRestrictionCode> findByRestrictionCode(String restrictionCode);
}
