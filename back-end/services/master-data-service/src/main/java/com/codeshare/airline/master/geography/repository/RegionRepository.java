package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.Region;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface RegionRepository extends CSMDataBaseRepository<Region, Long> {

    boolean existsByRegionCode(String regionCode);

    Optional<Region> findByRegionCode(String regionCode);
}
