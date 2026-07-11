package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;

import java.util.UUID;

public interface ScheduleInboundDeiRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {
}