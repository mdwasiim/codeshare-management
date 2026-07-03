package com.codeshare.airline.schedule.ingestion.orchestration.handler.parser;

import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
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

        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("SSIM lines are empty");
        }

        if (!(metadata instanceof SsimMetaDataDTO dto)) {
            throw new IllegalArgumentException("Invalid metadata for SSIM parser: "
                    + (metadata == null ? "null" : metadata.getClass().getName()));
        }
        ScheduleGroupedMessage scheduleGroupedMessage = scheduleParser.groupMessage(lines);
        // 🔥 SSIM parses FULL batch
        SSIMMessageDTO parsed = scheduleParser.parseMessage(scheduleGroupedMessage);

        return SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .parsedData(parsed)
                .metadata(dto)
                .profile(dto.getScheduleProfile())
                .messageLines(lines)
                .build();
    }
}
