package com.codeshare.airline.master.commercial.codeshare.repository;


import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareRoute;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CodeshareRouteRepository
        extends CSMDataBaseRepository<CodeshareRoute, UUID> {

    List<CodeshareRoute> findByAgreementId(UUID agreementId);

    boolean existsByAgreementIdAndOriginIdAndDestinationIdAndEffectiveFrom(
            UUID agreementId,
            UUID originId,
            UUID destinationId,
            LocalDate effectiveFrom
    );
}