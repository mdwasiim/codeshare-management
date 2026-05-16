package com.codeshare.airline.inbound.repositories.schedule;

import com.codeshare.airline.inbound.entities.schedule.ScheduleMessageEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ScheduleMessageRepository
        extends CSMDataBaseRepository<ScheduleMessageEntity, UUID>,
        JpaSpecificationExecutor<ScheduleMessageEntity> {

    long countByFile_FileId(UUID fileId);
}
