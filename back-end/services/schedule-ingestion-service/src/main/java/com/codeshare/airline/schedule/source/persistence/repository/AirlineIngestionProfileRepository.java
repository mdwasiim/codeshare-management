package com.codeshare.airline.schedule.source.persistence.repository;


import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionProfile;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirlineIngestionProfileRepository
        extends CSMDataBaseRepository<AirlineIngestionProfile, UUID> {

    Optional<AirlineIngestionProfile> findByAirlineCode(String airlineCode);

    @Query("""
       select distinct p
       from AirlineIngestionProfile p
       left join fetch p.channels
       """)
    List<AirlineIngestionProfile> findAllWithChannels();
}