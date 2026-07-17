package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveScheduleVersionEntity;
import com.codeshare.airline.schedule.live.domain.enums.ScheduleChangeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveScheduleVersionRepository extends CSMDataBaseRepository<LiveScheduleVersionEntity, Long> {

    List<LiveScheduleVersionEntity> findByFlightLegIdOrderByVersionNumberDesc(Long flightLegId);

    Optional<LiveScheduleVersionEntity> findTopByFlightLegIdOrderByVersionNumberDesc(Long flightLegId);

    List<LiveScheduleVersionEntity> findByFlightLegIdAndChangeType(Long flightLegId, ScheduleChangeType changeType);

    @Query("""
            SELECT MAX(v.versionNumber) FROM LiveScheduleVersionEntity v
            WHERE v.flightLeg.id = :legId
            """)
    Optional<Long> findMaxVersionNumberByLegId(@Param("legId") Long legId);
}
