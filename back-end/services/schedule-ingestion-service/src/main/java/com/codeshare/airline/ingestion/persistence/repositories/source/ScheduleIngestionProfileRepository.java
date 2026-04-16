package com.codeshare.airline.ingestion.persistence.repositories.source;


import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionProfileEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleIngestionProfileRepository
        extends CSMDataBaseRepository<ScheduleIngestionProfileEntity, UUID> {

    Optional<ScheduleIngestionProfileEntity> findByAirlineCode(String airlineCode);

    @Query("""
       select distinct p
       from ScheduleIngestionProfileEntity p
       left join fetch p.channels
       """)
    List<ScheduleIngestionProfileEntity> findAllWithChannels();
}