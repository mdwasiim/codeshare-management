package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightLegEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveFlightLegRepository extends CSMDataBaseRepository<LiveFlightLegEntity, UUID> {

    List<LiveFlightLegEntity> findByFlightIdOrderByLegSequenceNumberAsc(UUID flightId);

    List<LiveFlightLegEntity> findByFlightIdAndLegStatus(UUID flightId, LiveScheduleStatus legStatus);

    Optional<LiveFlightLegEntity> findByFlightIdAndLegSequenceNumberAndPeriodStartAndPeriodEndAndDaysOfOperation(
            UUID flightId,
            Integer legSequenceNumber,
            LocalDate periodStart,
            LocalDate periodEnd,
            String daysOfOperation
    );

    List<LiveFlightLegEntity> findByDepartureStationAndArrivalStationAndLegStatus(
            String departureStation,
            String arrivalStation,
            LiveScheduleStatus legStatus
    );

    @Query("""
            SELECT l FROM LiveFlightLegEntity l
            WHERE l.flight.id = :flightId
              AND l.legStatus = 'ACTIVE'
              AND l.periodStart <= :date
              AND l.periodEnd   >= :date
            ORDER BY l.legSequenceNumber ASC
            """)
    List<LiveFlightLegEntity> findActiveLegsForFlightOnDate(
            @Param("flightId") UUID flightId,
            @Param("date")     LocalDate date
    );

    @Query("""
            SELECT l FROM LiveFlightLegEntity l
            JOIN l.flight f
            WHERE f.airlineCode  = :airlineCode
              AND f.flightNumber = :flightNumber
              AND l.legStatus    = 'ACTIVE'
              AND l.periodStart <= :date
              AND l.periodEnd   >= :date
            ORDER BY l.legSequenceNumber ASC
            """)
    List<LiveFlightLegEntity> findActiveLegsByFlightNumberAndDate(
            @Param("airlineCode")  String airlineCode,
            @Param("flightNumber") String flightNumber,
            @Param("date")         LocalDate date
    );

}
