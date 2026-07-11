package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.SchedulePriority;

import java.util.Optional;
import java.util.UUID;

public interface SchedulePriorityRepository extends CSMDataBaseRepository<SchedulePriority, UUID> {

    Optional<SchedulePriority> findByPriorityCode(String priorityCode);
}
