package com.codeshare.airline.schedule.ingestion.persistence.repositories.source;


import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleIngestionChannelRepository
        extends CSMDataBaseRepository<ScheduleIngestionChannelEntity, UUID> {

    List<ScheduleIngestionChannelEntity> findByProfileId(UUID profileId);
}