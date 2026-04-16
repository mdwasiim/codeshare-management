package com.codeshare.airline.ingestion.persistence.services.common;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.source.inbound.ScheduleSourceFile;

public interface ScheduleFileService {

    <T extends ScheduleFileMetaDataDTO> T createInbound(ScheduleSourceFile file, MessageType type);

    void updateScheduleStatus(ScheduleFileMetaDataDTO metadata, ProcessingStatus status);
}
