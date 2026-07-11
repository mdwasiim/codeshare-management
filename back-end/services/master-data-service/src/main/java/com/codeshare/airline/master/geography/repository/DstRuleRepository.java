package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DstRuleRepository
        extends CSMDataBaseRepository<TimezoneDLS, UUID> {
    boolean existsByTimezone_TzIdentifierAndEffectiveFrom(String tzIdentifier, LocalDateTime effectiveFrom);
}
