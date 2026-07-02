package com.codeshare.airline.inbound.services.common;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;

public interface ScheduleFileService {

    <T extends ScheduleFileMetaDataDTO> T createInbound(ScheduleSourceFile file, MessageType type);

    void updateScheduleStatus(ScheduleFileMetaDataDTO metadata, ProcessingStatus status);
}
