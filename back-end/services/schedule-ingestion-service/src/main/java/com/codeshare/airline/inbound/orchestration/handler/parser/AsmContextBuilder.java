package com.codeshare.airline.inbound.orchestration.handler.parser;

import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.enums.AsmMessageType;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.parsers.MessageParser;
import com.codeshare.airline.inbound.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
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

        String firstLine = lines.get(0);

        if (firstLine.contains("NEW")) return AsmMessageType.NEW;
        if (firstLine.contains("CNL")) return AsmMessageType.CNL;
        if (firstLine.contains("RPL")) return AsmMessageType.RPL;

        return null;
    }
}