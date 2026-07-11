package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleComparisonRunEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ComparisonStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleComparisonRunRepository extends CSMDataBaseRepository<ScheduleComparisonRunEntity, UUID> {

    Optional<ScheduleComparisonRunEntity> findByIngestedMessageId(UUID ingestedMessageId);

    List<ScheduleComparisonRunEntity> findBySourceFileId(UUID sourceFileId);

    List<ScheduleComparisonRunEntity> findByAirlineCodeAndStatus(String airlineCode, ComparisonStatus status);

    List<ScheduleComparisonRunEntity> findByAirlineCodeAndSourceType(String airlineCode, MessageType sourceType);

    @Query("""
            SELECT r FROM ScheduleComparisonRunEntity r
            WHERE r.status IN ('PENDING', 'IN_PROGRESS')
            ORDER BY r.startedAt ASC
            """)
    List<ScheduleComparisonRunEntity> findActiveRuns();

    @Query("""
            SELECT r FROM ScheduleComparisonRunEntity r
            WHERE r.airlineCode = :airlineCode
            ORDER BY r.startedAt DESC
            """)
    List<ScheduleComparisonRunEntity> findLatestRunsForAirline(@Param("airlineCode") String airlineCode);
}
