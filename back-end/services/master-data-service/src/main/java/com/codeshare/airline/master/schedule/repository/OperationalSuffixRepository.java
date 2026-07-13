package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.OperationalSuffix;

import java.util.Optional;

public interface OperationalSuffixRepository extends CSMDataBaseRepository<OperationalSuffix, Long> {

    Optional<OperationalSuffix> findBySuffixCode(String suffixCode);
}
