package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleStatus;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleStatusRepository extends CSMDataBaseRepository<ScheduleStatus, UUID> {

    Optional<ScheduleStatus> findByScheduleStatusCode(String scheduleStatusCode);
}
