package com.codeshare.airline.master.commercial.codeshare.repository;

import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareDeiRule;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CodeshareDeiRuleRepository
        extends CSMDataBaseRepository<CodeshareDeiRule, UUID> {

    List<CodeshareDeiRule> findByFlightMappingId(UUID mappingId);

    boolean existsByFlightMappingIdAndDeiIdAndEffectiveFrom(
            UUID mappingId,
            UUID deiId,
            LocalDate effectiveFrom
    );
}