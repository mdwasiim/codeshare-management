package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.Region;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends CSMDataBaseRepository<Region, UUID> {

    boolean existsByCode(String code);

    Optional<Region> findByRegionCode(String mea);
}