package com.codeshare.airline.master.georegion.repository;

import com.codeshare.airline.master.georegion.entities.TimezoneDLS;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface DstRuleRepository
        extends CSMDataBaseRepository<TimezoneDLS, UUID> {
}