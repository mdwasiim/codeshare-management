package com.codeshare.airline.ingestion.orchestration.handler.parser;

import com.codeshare.airline.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.ingestion.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.ingestion.persistence.dto.ssim.SsimMetaDataDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SsimContextBuilder implements MessageParser<SsimIngestionContext> {

    private final ScheduleParser<SSIMMessageDTO> scheduleParser;

    public SsimContextBuilder(
            @Qualifier("ssimMessageParser")
            ScheduleParser<SSIMMessageDTO> scheduleParser) {
        this.scheduleParser = scheduleParser;
    }

    @Override
    public MessageType supportedType() {
        return MessageType.SSIM;
    }

    @Override
    public SsimIngestionContext parse(List<String> lines, ScheduleFileMetaDataDTO metadata) {

        if (!(metadata instanceof SsimMetaDataDTO dto)) {
            throw new IllegalArgumentException("Invalid metadata for SSIM parser");
        }
        ScheduleGroupedMessage scheduleGroupedMessage = scheduleParser.groupMessage(lines);
        // 🔥 SSIM parses FULL batch
        SSIMMessageDTO parsed = scheduleParser.parseMessage(scheduleGroupedMessage);

        return SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .parsedData(parsed)
                .metadata(dto)
                .messageLines(lines)
                .build();
    }
}