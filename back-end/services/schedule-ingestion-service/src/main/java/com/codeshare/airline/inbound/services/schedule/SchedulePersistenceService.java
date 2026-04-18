package com.codeshare.airline.inbound.services.schedule;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;

public interface SchedulePersistenceService {

    void save(ScheduleMessageDTO context, ScheduleFileMetaDataDTO scheduleFileMetaDataDTO);
}
