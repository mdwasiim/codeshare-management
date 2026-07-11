package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.OperationalSuffix;

import java.util.Optional;
import java.util.UUID;

public interface OperationalSuffixRepository extends CSMDataBaseRepository<OperationalSuffix, UUID> {

    Optional<OperationalSuffix> findBySuffixCode(String suffixCode);
}
