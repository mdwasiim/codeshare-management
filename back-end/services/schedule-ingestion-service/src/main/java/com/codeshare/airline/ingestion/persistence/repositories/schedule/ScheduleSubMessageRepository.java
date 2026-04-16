package com.codeshare.airline.ingestion.persistence.repositories.schedule;


import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleMessageEntity;

import java.util.UUID;

public interface ScheduleSubMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID> {
}