package com.codeshare.airline.schedule.ingestion.persistence.repositories.error;

import com.codeshare.airline.schedule.ingestion.persistence.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleErrorRepository
        extends CSMDataBaseRepository<ScheduleErrorEntity, UUID> {
}
