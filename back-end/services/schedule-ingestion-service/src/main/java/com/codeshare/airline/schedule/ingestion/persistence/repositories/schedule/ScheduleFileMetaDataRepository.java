package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleFileMetaDataRepository
        extends CSMDataBaseRepository<ScheduleFileMetaDataEntity, UUID>,
        JpaSpecificationExecutor<ScheduleFileMetaDataEntity> {

    Optional<ScheduleFileMetaDataEntity> findByFileId(UUID fileId);

}
