package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface DstRuleRepository
        extends CSMDataBaseRepository<TimezoneDLS, UUID> {
}