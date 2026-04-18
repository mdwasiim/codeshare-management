package com.codeshare.airline.inbound.repositories.error;

import com.codeshare.airline.inbound.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface ScheduleErrorRepository
        extends CSMDataBaseRepository<ScheduleErrorEntity, UUID> {
}
