package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionQualifier;


public interface TrafficRestrictionQualifierRepository extends CSMDataBaseRepository<TrafficRestrictionQualifier, Long> {
    boolean existsByTrafficRestrictionCode_RestrictionCodeAndQualifierCode(String restrictionCode, String qualifierCode);
}
