package com.codeshare.airline.inbound.repositories.schedule;


import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.schedule.ScheduleMessageEntity;

import java.util.UUID;

public interface ScheduleSubMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID> {
}