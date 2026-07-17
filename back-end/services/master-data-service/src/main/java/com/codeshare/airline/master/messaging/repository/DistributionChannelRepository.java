package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.master.messaging.entities.DistributionChannel;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface DistributionChannelRepository
        extends CSMDataBaseRepository<DistributionChannel, Long> {

    boolean existsByChannelCode(String channelCode);

    Optional<DistributionChannel> findByChannelCode(String channelCode);
}