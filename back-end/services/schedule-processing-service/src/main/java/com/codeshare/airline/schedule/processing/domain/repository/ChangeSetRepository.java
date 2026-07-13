package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ChangeSetEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ComparisonStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChangeSetRepository extends CSMDataBaseRepository<ChangeSetEntity, Long> {

    Optional<ChangeSetEntity> findByChangeSetId(UUID changeSetId);

    Optional<ChangeSetEntity> findByImportBatchId(UUID importBatchId);

    List<ChangeSetEntity> findByImportedScheduleId(UUID importedScheduleId);

    List<ChangeSetEntity> findByAirlineCodeAndStatus(String airlineCode, ComparisonStatus status);

    List<ChangeSetEntity> findByAirlineCodeAndSourceType(String airlineCode, MessageType sourceType);

    @Query("""
            select c from ChangeSetEntity c
            where c.status in ('PENDING', 'IN_PROGRESS')
            order by c.startedAt asc
            """)
    List<ChangeSetEntity> findActiveChangeSets();

    @Query("""
            select c from ChangeSetEntity c
            where c.airlineCode = :airlineCode
            order by c.startedAt desc
            """)
    List<ChangeSetEntity> findLatestChangeSetsForAirline(@Param("airlineCode") String airlineCode);
}
