package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.inbound.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleFileMetaDataRepository
        extends CSMDataBaseRepository<ScheduleFileMetaDataEntity, UUID> {

    Optional<ScheduleFileMetaDataEntity> findByFileId(UUID fileId);

}