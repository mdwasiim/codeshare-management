package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;


import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleMessageEntity;

import java.util.UUID;

public interface ScheduleSubMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID> {
}