package com.codeshare.airline.distribution.engine.domain.repository;

import com.codeshare.airline.distribution.engine.domain.entity.DistributionJobEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DistributionJobRepository extends CSMDataBaseRepository<DistributionJobEntity, Long> {

    Optional<DistributionJobEntity> findByDistributionRequestId(UUID distributionRequestId);
}
