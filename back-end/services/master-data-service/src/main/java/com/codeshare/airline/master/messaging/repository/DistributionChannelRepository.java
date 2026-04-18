package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.eitities.DistributionChannel;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DistributionChannelRepository
        extends CSMDataBaseRepository<DistributionChannel, UUID> {

    boolean existsByChannelCode(String channelCode);

    Optional<DistributionChannel> findByChannelCode(String channelCode);
}