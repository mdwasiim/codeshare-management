package com.codeshare.airline.schedule.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundLeg;

import java.util.UUID;

public interface ScheduleInboundLegRepository
        extends CSMDataBaseRepository<ScheduleInboundLeg, UUID> {
}