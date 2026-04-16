package com.codeshare.airline.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleDataElementRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, UUID> {

}