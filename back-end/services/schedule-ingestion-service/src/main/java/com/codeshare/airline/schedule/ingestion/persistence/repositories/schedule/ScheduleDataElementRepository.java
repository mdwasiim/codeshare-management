package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleDataElementEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;


public interface ScheduleDataElementRepository
        extends CSMDataBaseRepository<ScheduleDataElementEntity, Long> {

}
