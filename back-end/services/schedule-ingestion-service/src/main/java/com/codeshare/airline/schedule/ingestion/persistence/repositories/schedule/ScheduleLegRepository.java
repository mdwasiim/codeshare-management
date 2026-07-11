package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleLegEntity;

import java.util.UUID;

public interface ScheduleLegRepository
        extends CSMDataBaseRepository<ScheduleLegEntity, UUID> {
}