package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;

import java.util.UUID;

public interface TrafficRestrictionCodeRepository extends CSMDataBaseRepository<TrafficRestrictionCode, UUID> {
}
