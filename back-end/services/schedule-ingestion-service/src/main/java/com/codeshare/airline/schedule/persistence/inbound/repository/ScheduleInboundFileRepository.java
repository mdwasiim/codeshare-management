package com.codeshare.airline.schedule.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleInboundFileRepository
        extends CSMDataBaseRepository<ScheduleInboundFile, UUID> {

    Optional<ScheduleInboundFile> findByFileId(String fileId);

    boolean existsByFileId(String fileId);
}