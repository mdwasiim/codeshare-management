package com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFlightEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ScheduleFlightRepository
        extends CSMDataBaseRepository<ScheduleFlightEntity, UUID>,
        JpaSpecificationExecutor<ScheduleFlightEntity> {

    long countBySubMessage_Message_File_FileId(UUID fileId);

    long countBySubMessage_Message_Id(UUID messageId);
}
