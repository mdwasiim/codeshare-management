package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleType;

import java.util.Optional;

public interface ScheduleTypeRepository extends CSMDataBaseRepository<ScheduleType, Long> {

    boolean existsByScheduleTypeCode(String scheduleTypeCode);

    Optional<ScheduleType> findByScheduleTypeCode(String scheduleTypeCode);
}
