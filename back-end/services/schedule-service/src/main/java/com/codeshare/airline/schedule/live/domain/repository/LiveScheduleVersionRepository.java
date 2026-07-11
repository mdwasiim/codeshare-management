package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveScheduleVersionEntity;
import com.codeshare.airline.schedule.live.domain.enums.ScheduleChangeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveScheduleVersionRepository extends CSMDataBaseRepository<LiveScheduleVersionEntity, UUID> {

    List<LiveScheduleVersionEntity> findByFlightLegIdOrderByVersionNumberDesc(UUID flightLegId);

    Optional<LiveScheduleVersionEntity> findTopByFlightLegIdOrderByVersionNumberDesc(UUID flightLegId);

    List<LiveScheduleVersionEntity> findByFlightLegIdAndChangeType(UUID flightLegId, ScheduleChangeType changeType);

    @Query("""
            SELECT MAX(v.versionNumber) FROM LiveScheduleVersionEntity v
            WHERE v.flightLeg.id = :legId
            """)
    Optional<Long> findMaxVersionNumberByLegId(@Param("legId") UUID legId);
}
