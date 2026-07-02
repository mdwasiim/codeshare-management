package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleType;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleTypeRepository extends CSMDataBaseRepository<ScheduleType, UUID> {

    boolean existsByScheduleTypeCode(String scheduleTypeCode);

    Optional<ScheduleType> findByScheduleTypeCode(String scheduleTypeCode);
}
