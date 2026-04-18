package com.codeshare.airline.master.airport.georegion.repository;

import com.codeshare.airline.master.airport.georegion.eitities.Region;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends CSMDataBaseRepository<Region, UUID> {

    boolean existsByCode(String code);

    Optional<Region> findByRegionCode(String mea);
}