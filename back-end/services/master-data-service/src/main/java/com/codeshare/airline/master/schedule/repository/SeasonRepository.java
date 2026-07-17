package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.master.schedule.entities.Season;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface SeasonRepository extends CSMDataBaseRepository<Season, Long> {

    boolean existsBySeasonCode(String seasonCode);

    Optional<Season> findBySeasonCode(String seasonCode);
}