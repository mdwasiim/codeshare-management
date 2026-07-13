package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.ActiveScheduleEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveScheduleRepository extends CSMDataBaseRepository<ActiveScheduleEntity, Long> {

    Optional<ActiveScheduleEntity> findByAirlineCode(String airlineCode);
}
