package com.codeshare.airline.data.codeshare.repository;

import com.codeshare.airline.data.codeshare.eitities.CodeshareDayRule;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CodeshareDayRuleRepository
        extends CSMDataBaseRepository<CodeshareDayRule, UUID> {

    List<CodeshareDayRule> findByFlightMappingId(UUID mappingId);

    boolean existsByFlightMappingIdAndEffectiveFrom(
            UUID mappingId,
            LocalDate effectiveFrom
    );
}