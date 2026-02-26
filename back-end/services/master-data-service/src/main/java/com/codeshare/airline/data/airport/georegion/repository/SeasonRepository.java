package com.codeshare.airline.data.airport.georegion.repository;

import com.codeshare.airline.data.airport.georegion.eitities.Season;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface SeasonRepository extends CSMDataBaseRepository<Season, UUID> {

    boolean existsBySeasonCode(String seasonCode);

    Optional<Season> findBySeasonCode(String seasonCode);
}