package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.inbound.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleFileMetaDataRepository
        extends CSMDataBaseRepository<ScheduleFileMetaDataEntity, UUID>,
        JpaSpecificationExecutor<ScheduleFileMetaDataEntity> {

    Optional<ScheduleFileMetaDataEntity> findByFileId(UUID fileId);

}
