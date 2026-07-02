package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleChannel;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleChannelRepository extends CSMDataBaseRepository<ScheduleChannel, UUID> {

    Optional<ScheduleChannel> findByChannelCode(String channelCode);
}
