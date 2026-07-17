package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.SchedulePriority;

import java.util.Optional;

public interface SchedulePriorityRepository extends CSMDataBaseRepository<SchedulePriority, Long> {

    Optional<SchedulePriority> findByPriorityCode(String priorityCode);
}
