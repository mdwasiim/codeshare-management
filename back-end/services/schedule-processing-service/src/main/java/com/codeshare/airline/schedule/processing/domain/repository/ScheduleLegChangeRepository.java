package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleLegChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.LegChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleLegChangeRepository extends CSMDataBaseRepository<ScheduleLegChangeEntity, UUID> {

    List<ScheduleLegChangeEntity> findByFlightChangeId(UUID flightChangeId);

    List<ScheduleLegChangeEntity> findByFlightChangeIdAndChangeType(UUID flightChangeId, LegChangeType changeType);

    List<ScheduleLegChangeEntity> findByLiveLegId(UUID liveLegId);

    List<ScheduleLegChangeEntity> findByChangeSetStatus(ChangeSetStatus changeSetStatus);

    List<ScheduleLegChangeEntity> findByChangeTypeAndChangeSetStatus(LegChangeType changeType, ChangeSetStatus changeSetStatus);

    @Query("""
            SELECT l FROM ScheduleLegChangeEntity l
            JOIN l.flightChange f
            WHERE f.airlineCode  = :airlineCode
              AND f.flightNumber = :flightNumber
              AND l.legSequenceNumber = :legSeq
              AND l.changeSetStatus = 'PENDING'
            ORDER BY l.createdAt ASC
            """)
    List<ScheduleLegChangeEntity> findPendingChangeSetEntriesForLeg(
            @Param("airlineCode")  String airlineCode,
            @Param("flightNumber") String flightNumber,
            @Param("legSeq")       Integer legSeq
    );
}

