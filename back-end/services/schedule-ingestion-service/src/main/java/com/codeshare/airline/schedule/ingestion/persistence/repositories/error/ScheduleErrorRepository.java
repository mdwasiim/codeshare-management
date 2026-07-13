package com.codeshare.airline.schedule.ingestion.persistence.repositories.error;

import com.codeshare.airline.schedule.ingestion.persistence.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;


public interface ScheduleErrorRepository
        extends CSMDataBaseRepository<ScheduleErrorEntity, Long> {
}
