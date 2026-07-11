package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionQualifier;

import java.util.UUID;

public interface TrafficRestrictionQualifierRepository extends CSMDataBaseRepository<TrafficRestrictionQualifier, UUID> {
    boolean existsByTrafficRestrictionCode_RestrictionCodeAndQualifierCode(String restrictionCode, String qualifierCode);
}
