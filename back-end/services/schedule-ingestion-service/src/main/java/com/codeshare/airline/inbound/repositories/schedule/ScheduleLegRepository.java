package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.schedule.ScheduleLegEntity;

import java.util.UUID;

public interface ScheduleLegRepository
        extends CSMDataBaseRepository<ScheduleLegEntity, UUID> {
}