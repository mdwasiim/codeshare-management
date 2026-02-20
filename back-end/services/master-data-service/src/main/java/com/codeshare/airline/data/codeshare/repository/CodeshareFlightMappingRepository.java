package com.codeshare.airline.data.codeshare.repository;

import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CodeshareFlightMappingRepository
        extends CSMDataBaseRepository<CodeshareFlightMapping, UUID> {

    List<CodeshareFlightMapping> findByAgreementId(UUID agreementId);

    boolean existsByAgreementIdAndOperatingFlightNumberAndMarketingFlightNumberAndEffectiveFrom(
            UUID agreementId,
            String operatingFlightNumber,
            String marketingFlightNumber,
            LocalDate effectiveFrom
    );
}