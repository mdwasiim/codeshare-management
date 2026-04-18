package com.codeshare.airline.inbound.repositories.source;


import com.codeshare.airline.inbound.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleIngestionChannelRepository
        extends CSMDataBaseRepository<ScheduleIngestionChannelEntity, UUID> {

    List<ScheduleIngestionChannelEntity> findByProfileId(UUID profileId);
}