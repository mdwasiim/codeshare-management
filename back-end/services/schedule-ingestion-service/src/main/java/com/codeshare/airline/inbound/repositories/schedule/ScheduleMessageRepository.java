package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.inbound.entities.schedule.ScheduleMessageEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID> {

}