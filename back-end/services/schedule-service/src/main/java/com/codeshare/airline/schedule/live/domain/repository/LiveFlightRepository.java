package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LiveFlightRepository extends CSMDataBaseRepository<LiveFlightEntity, UUID> {

    Optional<LiveFlightEntity> findByAirlineCodeAndFlightNumberAndOperationalSuffixAndItineraryVariationId(
            String airlineCode,
            String flightNumber,
            String operationalSuffix,
            String itineraryVariationId
    );

    List<LiveFlightEntity> findByAirlineCode(String airlineCode);

    List<LiveFlightEntity> findByAirlineCodeAndFlightStatus(String airlineCode, LiveScheduleStatus flightStatus);

    List<LiveFlightEntity> findByAirlineCodeAndFlightNumberAndFlightStatus(
            String airlineCode,
            String flightNumber,
            LiveScheduleStatus flightStatus
    );
}
