package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleCategory;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleCategoryRepository extends CSMDataBaseRepository<ScheduleCategory, UUID> {

    Optional<ScheduleCategory> findByCategoryCode(String categoryCode);
}
