package com.codeshare.airline.schedule.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundDei;

import java.util.UUID;

public interface ScheduleInboundDeiRepository
        extends CSMDataBaseRepository<ScheduleInboundDei, UUID> {
}