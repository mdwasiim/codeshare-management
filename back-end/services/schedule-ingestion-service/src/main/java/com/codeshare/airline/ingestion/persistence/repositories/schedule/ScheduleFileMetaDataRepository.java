package com.codeshare.airline.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleFileMetaDataRepository
        extends CSMDataBaseRepository<ScheduleFileMetaDataEntity, UUID> {

    Optional<ScheduleFileMetaDataEntity> findByFileId(UUID fileId);

}