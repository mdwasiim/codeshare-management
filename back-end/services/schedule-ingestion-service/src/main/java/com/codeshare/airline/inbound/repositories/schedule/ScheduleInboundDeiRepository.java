package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.schedule.ScheduleDataElementEntity;

import java.util.UUID;

public interface ScheduleInboundDeiRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {
}