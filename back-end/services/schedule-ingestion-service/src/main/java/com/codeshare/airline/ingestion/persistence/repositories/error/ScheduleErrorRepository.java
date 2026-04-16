package com.codeshare.airline.ingestion.persistence.repositories.error;

import com.codeshare.airline.ingestion.persistence.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleErrorRepository
        extends CSMDataBaseRepository<ScheduleErrorEntity, UUID> {
}
