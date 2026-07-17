package com.codeshare.airline.master.aircraft.repository;

import com.codeshare.airline.master.aircraft.entities.AircraftCabinLayout;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftCabinLayoutRepository
        extends CSMDataBaseRepository<AircraftCabinLayout, Long> {

    List<AircraftCabinLayout> findByAircraftConfigurationId(Long configId);


    @Query("""
    SELECT COALESCE(SUM(c.seatCount), 0)
    FROM AircraftCabinLayout c
    WHERE c.aircraftConfiguration.id = :configId
""")
    int sumSeatCount(@Param("configId") Long configId);

}