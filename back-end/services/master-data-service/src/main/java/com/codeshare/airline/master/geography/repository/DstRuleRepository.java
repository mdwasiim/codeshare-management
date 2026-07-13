package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;

public interface DstRuleRepository
        extends CSMDataBaseRepository<TimezoneDLS, Long> {
    boolean existsByTimezone_TzIdentifierAndEffectiveFrom(String tzIdentifier, LocalDateTime effectiveFrom);
}
