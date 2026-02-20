package com.codeshare.airline.data.codeshare.repository;

import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeshareAgreementRepository
        extends CSMDataBaseRepository<CodeshareAgreement, UUID> {

    List<CodeshareAgreement> findByMarketingAirlineId(UUID airlineId);

    List<CodeshareAgreement> findByOperatingAirlineId(UUID airlineId);

    boolean existsByMarketingAirlineIdAndOperatingAirlineIdAndEffectiveFrom(
            UUID marketingId,
            UUID operatingId,
            LocalDate effectiveFrom
    );

    Optional<CodeshareAgreement> findByAgreementCode(String s);
}