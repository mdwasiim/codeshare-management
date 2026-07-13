package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleFlightChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleFlightChangeRepository extends CSMDataBaseRepository<ScheduleFlightChangeEntity, Long> {

    List<ScheduleFlightChangeEntity> findByChangeSet_ChangeSetId(UUID changeSetId);

    Optional<ScheduleFlightChangeEntity> findByChangeSet_ChangeSetIdAndAirlineCodeAndFlightNumberAndOperationalSuffixAndItineraryVariationId(
            UUID changeSetId,
            String airlineCode,
            String flightNumber,
            String operationalSuffix,
            String itineraryVariationId
    );

    List<ScheduleFlightChangeEntity> findByAirlineCodeAndFlightNumber(String airlineCode, String flightNumber);

    List<ScheduleFlightChangeEntity> findByAirlineCodeAndChangeSetStatus(String airlineCode, ChangeSetStatus changeSetStatus);

    List<ScheduleFlightChangeEntity> findByChangeSetStatus(ChangeSetStatus changeSetStatus);
}

