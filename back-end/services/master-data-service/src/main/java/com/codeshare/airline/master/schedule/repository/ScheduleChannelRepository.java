package com.codeshare.airline.master.schedule.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.schedule.entities.ScheduleChannel;

import java.util.Optional;

public interface ScheduleChannelRepository extends CSMDataBaseRepository<ScheduleChannel, Long> {

    Optional<ScheduleChannel> findByChannelCode(String channelCode);
}
