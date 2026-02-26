package com.codeshare.airline.schedule.source.persistence.repository;


import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;

import java.util.List;
import java.util.UUID;

public interface AirlineIngestionChannelRepository
        extends CSMDataBaseRepository<AirlineIngestionChannel, UUID> {

    List<AirlineIngestionChannel> findByProfileId(UUID profileId);
}