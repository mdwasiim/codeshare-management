package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TimeMode;


public interface TimeModeRepository extends CSMDataBaseRepository<TimeMode, Long> {
    boolean existsByTimeModeCode(String timeModeCode);
}
