package com.codeshare.airline.schedule.ingestion.orchestration.context;

import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SsmContextBuilder implements MessageParser<SsmIngestionContext> {

    private final ScheduleParser<ScheduleMessageDTO> scheduleParser;

    public SsmContextBuilder(
            @Qualifier("ssmMessageParser")
            ScheduleParser<ScheduleMessageDTO> scheduleParser) {
        this.scheduleParser = scheduleParser;
    }

    @Override
    public MessageType supportedType() {
        return MessageType.SSM;
    }

    @Override
    public SsmIngestionContext parse(List<String> lines, ScheduleFileMetaDataDTO metadata) {

        ScheduleGroupedMessage scheduleGroupedMessage = scheduleParser.groupMessage(lines);

        ScheduleMessageDTO envelope = scheduleParser.parseMessage(scheduleGroupedMessage);

        /* ================= METADATA ENRICHMENT ================= */

        envelope.setMessageType(metadata.getMessageType());
        envelope.setSource(metadata.getSourceType().name());
        envelope.setSender(metadata.getAirlineCode());
        envelope.setRecipient(metadata.getAirlineCode());

        return SsmIngestionContext.builder()
                .parsedData(envelope)
                .metadata(metadata)
                .messageLines(lines)
                .messageType(MessageType.SSM)
                .build();
    }
}
