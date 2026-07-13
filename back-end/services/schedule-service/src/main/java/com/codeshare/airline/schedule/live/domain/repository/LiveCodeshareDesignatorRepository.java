package com.codeshare.airline.schedule.live.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.live.domain.entity.LiveCodeshareDesignatorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiveCodeshareDesignatorRepository extends CSMDataBaseRepository<LiveCodeshareDesignatorEntity, Long> {

    List<LiveCodeshareDesignatorEntity> findByFlightLegIdOrderBySequenceOrderAsc(Long flightLegId);

    Optional<LiveCodeshareDesignatorEntity> findByFlightLegIdAndMarketingAirlineCodeAndMarketingFlightNumberAndBoardPointAndOffPoint(
            Long flightLegId,
            String marketingAirlineCode,
            String marketingFlightNumber,
            String boardPoint,
            String offPoint
    );

    List<LiveCodeshareDesignatorEntity> findByMarketingAirlineCode(String marketingAirlineCode);

    @Query("""
            SELECT cd FROM LiveCodeshareDesignatorEntity cd
            JOIN cd.flightLeg l
            JOIN l.flight f
            WHERE f.airlineCode  = :operatingAirlineCode
              AND f.flightNumber = :operatingFlightNumber
            ORDER BY cd.sequenceOrder ASC
            """)
    List<LiveCodeshareDesignatorEntity> findByOperatingFlight(
            @Param("operatingAirlineCode")  String operatingAirlineCode,
            @Param("operatingFlightNumber") String operatingFlightNumber
    );

    void deleteByFlightLegId(Long flightLegId);
}
