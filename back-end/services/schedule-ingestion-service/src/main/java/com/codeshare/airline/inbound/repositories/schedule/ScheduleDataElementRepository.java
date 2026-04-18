package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.inbound.entities.schedule.ScheduleDataElementEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleDataElementRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {

}