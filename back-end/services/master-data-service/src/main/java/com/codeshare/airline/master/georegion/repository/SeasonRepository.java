package com.codeshare.airline.master.georegion.repository;

import com.codeshare.airline.master.georegion.eitities.Season;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface SeasonRepository extends CSMDataBaseRepository<Season, UUID> {

    boolean existsBySeasonCode(String seasonCode);

    Optional<Season> findBySeasonCode(String seasonCode);
}