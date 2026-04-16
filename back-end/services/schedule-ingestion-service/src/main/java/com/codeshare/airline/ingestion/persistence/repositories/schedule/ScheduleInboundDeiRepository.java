package com.codeshare.airline.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;

import java.util.UUID;

public interface ScheduleInboundDeiRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {
}