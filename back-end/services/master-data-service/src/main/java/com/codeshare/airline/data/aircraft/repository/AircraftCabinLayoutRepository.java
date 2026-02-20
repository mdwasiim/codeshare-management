package com.codeshare.airline.data.aircraft.repository;

import com.codeshare.airline.data.aircraft.eitities.AircraftCabinLayout;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AircraftCabinLayoutRepository
        extends CSMDataBaseRepository<AircraftCabinLayout, UUID> {

    List<AircraftCabinLayout> findByAircraftConfigurationId(UUID configId);


    @Query("""
    SELECT COALESCE(SUM(c.seatCount), 0)
    FROM AircraftCabinLayout c
    WHERE c.aircraftConfiguration.id = :configId
""")
    int sumSeatCount(@Param("configId") UUID configId);

}