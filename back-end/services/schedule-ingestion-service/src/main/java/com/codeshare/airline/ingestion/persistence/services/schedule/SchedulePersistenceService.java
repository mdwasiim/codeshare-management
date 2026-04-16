package com.codeshare.airline.ingestion.persistence.services.schedule;

import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleMessageDTO;

public interface SchedulePersistenceService {

    void save(ScheduleMessageDTO context, ScheduleFileMetaDataDTO scheduleFileMetaDataDTO);
}
