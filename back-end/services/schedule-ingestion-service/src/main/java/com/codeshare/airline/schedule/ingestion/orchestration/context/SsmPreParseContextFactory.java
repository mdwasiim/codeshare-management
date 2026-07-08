package com.codeshare.airline.schedule.ingestion.orchestration.context;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SsmPreParseContextFactory implements PreParseContextFactory<SsmIngestionContext> {

    @Override
    public MessageType supportedType() {
        return MessageType.SSM;
    }

    @Override
    public SsmIngestionContext build(ScheduleFileMetaDataDTO metadata, List<String> lines) {
        return SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .metadata(metadata)
                .messageLines(lines)
                .build();
    }
}
