package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleStatus;

import java.util.Optional;

public interface ScheduleStatusRepository extends CSMDataBaseRepository<ScheduleStatus, Long> {

    Optional<ScheduleStatus> findByScheduleStatusCode(String scheduleStatusCode);
}
