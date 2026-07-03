package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleDataElementRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {

}