package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleMessageEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ScheduleMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID>,
        JpaSpecificationExecutor<ScheduleMessageEntity> {

    long countByFile_FileId(UUID fileId);
}
