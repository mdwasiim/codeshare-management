package com.codeshare.airline.data.messaging.repository;

import com.codeshare.airline.data.messaging.eitities.DistributionChannel;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DistributionChannelRepository
        extends CSMDataBaseRepository<DistributionChannel, UUID> {

    boolean existsByChannelCode(String channelCode);

    Optional<DistributionChannel> findByChannelCode(String channelCode);
}