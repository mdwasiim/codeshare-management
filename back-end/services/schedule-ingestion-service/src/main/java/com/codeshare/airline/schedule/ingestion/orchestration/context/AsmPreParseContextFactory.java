package com.codeshare.airline.schedule.ingestion.orchestration.context;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsmPreParseContextFactory implements PreParseContextFactory<AsmIngestionContext> {

    @Override
    public MessageType supportedType() {
        return MessageType.ASM;
    }

    @Override
    public AsmIngestionContext build(ScheduleFileMetaDataDTO metadata, List<String> lines) {
        return AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .metadata(metadata)
                .messageLines(lines)
                .build();
    }
}
