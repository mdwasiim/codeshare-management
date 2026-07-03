package com.codeshare.airline.master.flight.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.flight.schedule.entities.TimeMode;

import java.util.UUID;

public interface TimeModeRepository extends CSMDataBaseRepository<TimeMode, UUID> {
}
