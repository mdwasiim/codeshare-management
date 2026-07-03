package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionQualifier;

import java.util.UUID;

public interface TrafficRestrictionQualifierRepository extends CSMDataBaseRepository<TrafficRestrictionQualifier, UUID> {
}
