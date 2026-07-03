package com.codeshare.airline.schedule.ingestion.persistence.services.schedule;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;

public interface SchedulePersistenceService {

    void save(ScheduleMessageDTO context, ScheduleFileMetaDataDTO scheduleFileMetaDataDTO);
}
