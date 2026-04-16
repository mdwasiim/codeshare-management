package com.codeshare.airline.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleFlightEntity;

import java.util.UUID;

public interface ScheduleFlightRepository
        extends CSMDataBaseRepository<ScheduleFlightEntity, UUID> {

}