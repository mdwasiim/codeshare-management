package com.codeshare.airline.schedule.persistence.inbound.repository;


import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundBlock;

import java.util.UUID;

public interface ScheduleInboundBlockRepository
        extends CSMDataBaseRepository<ScheduleInboundBlock, UUID> {
}