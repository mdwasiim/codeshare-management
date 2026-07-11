package com.codeshare.airline.schedule.ingestion.orchestration;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.source.model.ExchangeConstants;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public abstract class ScheduleIngestionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

        ScheduleSourceFile sourceFile = exchange.getMessage().getBody(ScheduleSourceFile.class);

        if (sourceFile == null) {
            throw new IllegalStateException(
                    "ScheduleSourceFile missing. ExchangeId=" + exchange.getExchangeId()
            );
        }

        MessageType type = sourceFile.getMessageType();

        if (type == null) {
            throw new IllegalStateException(
                    "MessageType is missing for file=" + sourceFile.getFileName()
            );
        }

        if (!supports(type)) {
            throw new IllegalStateException("Unsupported message type " + type);
        }

        log.info("Processing started | type={} file={} checksum={} exchangeId={}",
                type,
                sourceFile.getFileName(),
                sourceFile.getChecksum(),
                exchange.getExchangeId());

        try {
            ProcessingStatus status = processSourceFile(sourceFile);
            exchange.setProperty(ExchangeConstants.PROCESS_STATUS, status);

        } catch (Exception ex) {

            exchange.setProperty(ExchangeConstants.PROCESS_STATUS, ProcessingStatus.FAILED);
            exchange.setProperty(ExchangeConstants.ERROR_MESSAGE, ex.getMessage());

            log.error("Processing failed | file={} exchangeId={}", sourceFile.getFileName(), exchange.getExchangeId(), ex);

            throw ex;
        }
    }

    protected abstract boolean supports(MessageType type);

    protected abstract ProcessingStatus processSourceFile(ScheduleSourceFile sourceFile);
}
