package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFlightEntity;

import java.util.UUID;

public interface ScheduleFlightRepository
        extends CSMDataBaseRepository<ScheduleFlightEntity, UUID> {

}