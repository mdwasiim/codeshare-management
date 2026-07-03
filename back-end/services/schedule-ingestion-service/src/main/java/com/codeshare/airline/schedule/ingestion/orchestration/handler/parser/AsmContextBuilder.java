package com.codeshare.airline.schedule.ingestion.orchestration.handler.parser;

import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsmContextBuilder implements MessageParser<AsmIngestionContext> {

    private final ScheduleParser<ScheduleMessageDTO> scheduleParser;

    public AsmContextBuilder(
            @Qualifier("asmMessageParser")
            ScheduleParser<ScheduleMessageDTO> scheduleParser) {
        this.scheduleParser = scheduleParser;
    }

    @Override
    public MessageType supportedType() {
        return MessageType.ASM;
    }

    @Override
    public AsmIngestionContext parse(List<String> lines, ScheduleFileMetaDataDTO metadata) {

        ScheduleGroupedMessage scheduleGroupedMessage = scheduleParser.groupMessage(lines);

        ScheduleMessageDTO envelope = scheduleParser.parseMessage(scheduleGroupedMessage);

        /* ================= METADATA ENRICHMENT ================= */

        envelope.setMessageType(metadata.getMessageType());
        envelope.setSource(metadata.getSourceType().name());
        envelope.setSender(metadata.getAirlineCode());
        envelope.setRecipient(metadata.getAirlineCode());

        return AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .parsedData(envelope)
                .metadata(metadata)
                .messageLines(lines)
                .subMessageType(extractSubType(lines))
                .build();
    }

    private AsmMessageType extractSubType(List<String> lines) {

        if (lines == null || lines.isEmpty()) return null;

        for (String line : lines) {
            String token = extractFirstToken(line);
            AsmMessageType type = AsmMessageType.from(token);

            if (type != null && type != AsmMessageType.UNKNOWN) {
                return type;
            }
        }

        return null;
    }

    private String extractFirstToken(String line) {
        if (line == null) return null;
        String trimmed = line.trim();
        int idx = trimmed.indexOf(' ');
        return idx > 0 ? trimmed.substring(0, idx) : trimmed;
    }
}
