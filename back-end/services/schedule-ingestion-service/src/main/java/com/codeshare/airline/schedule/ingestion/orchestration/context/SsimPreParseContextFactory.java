package com.codeshare.airline.schedule.ingestion.orchestration.context;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SsimPreParseContextFactory implements PreParseContextFactory<SsimIngestionContext> {

    @Override
    public MessageType supportedType() {
        return MessageType.SSIM;
    }

    @Override
    public SsimIngestionContext build(ScheduleFileMetaDataDTO metadata, List<String> lines) {
        if (!(metadata instanceof SsimMetaDataDTO dto)) {
            throw new IllegalArgumentException("Invalid metadata for SSIM context: "
                    + (metadata == null ? "null" : metadata.getClass().getName()));
        }

        return SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .metadata(dto)
                .messageLines(lines)
                .build();
    }
}
