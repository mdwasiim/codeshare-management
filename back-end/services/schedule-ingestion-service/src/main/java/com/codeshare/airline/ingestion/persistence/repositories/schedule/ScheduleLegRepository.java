package com.codeshare.airline.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleLegEntity;

import java.util.UUID;

public interface ScheduleLegRepository
        extends CSMDataBaseRepository<ScheduleLegEntity, UUID> {
}