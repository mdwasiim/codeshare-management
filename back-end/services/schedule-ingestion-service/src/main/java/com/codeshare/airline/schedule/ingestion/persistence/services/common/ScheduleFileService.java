package com.codeshare.airline.schedule.ingestion.persistence.services.common;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;

public interface ScheduleFileService {

    <T extends ScheduleFileMetaDataDTO> T createInbound(ScheduleSourceFile file, MessageType type);

    void updateScheduleStatus(ScheduleFileMetaDataDTO metadata, ProcessingStatus status);
}
