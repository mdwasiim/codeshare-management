package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleSource;

import java.util.Optional;

public interface ScheduleSourceRepository extends CSMDataBaseRepository<ScheduleSource, Long> {

    Optional<ScheduleSource> findBySourceCode(String sourceCode);
}
