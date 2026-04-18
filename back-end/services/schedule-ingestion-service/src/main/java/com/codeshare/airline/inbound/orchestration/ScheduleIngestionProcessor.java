package com.codeshare.airline.inbound.orchestration;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.inbound.source.inbound.ExchangeConstants;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleIngestionProcessor implements Processor {

    private final ScheduleChapterProcessor processor;

    @Override
    public void process(Exchange exchange) {

        ScheduleSourceFile sourceFile = exchange.getMessage().getBody(ScheduleSourceFile.class);

        if (sourceFile == null) {
            throw new IllegalStateException(
                    "ScheduleSourceFile missing. ExchangeId="
                            + exchange.getExchangeId()
            );
        }

        MessageType type = sourceFile.getMessageType();

        if (type == null) {
            throw new IllegalStateException(
                    "MessageType is missing for file="
                            + sourceFile.getFileName()
            );
        }

        log.info(" Processing started | type={} file={} exchangeId={}",
                type, sourceFile.getFileName(),
                            exchange.getExchangeId());

        try {
            processor.process(sourceFile);
            // mark success
            exchange.setProperty(ExchangeConstants.PROCESS_STATUS, ProcessingStatus.SUCCESS);

        } catch (Exception ex) {

            exchange.setProperty(ExchangeConstants.PROCESS_STATUS, ProcessingStatus.FAILED);
            exchange.setProperty(ExchangeConstants.ERROR_MESSAGE, ex.getMessage());

            log.error(" Processing failed | file={} exchangeId={}", sourceFile.getFileName(), exchange.getExchangeId(), ex);

            throw ex;

        } finally {
            computeChecksum(exchange, sourceFile);
        }
    }

    /**
     * Compute checksum (metadata only — no body mutation)
     */
    private void computeChecksum(Exchange exchange, ScheduleSourceFile sourceFile) {

        MessageDigest digest = exchange.getProperty(ExchangeConstants.CHECKSUM_DIGEST, MessageDigest.class);

        if (digest == null) {
            log.warn("⚠ Checksum digest not found | file={}",
                    sourceFile.getFileName());
            return;
        }

        String checksum = java.util.HexFormat.of().formatHex(digest.digest());

        //  store as metadata (safe)
        exchange.setProperty(ExchangeConstants.CHECKSUM, checksum);

        log.info(" Computed checksum={} for file={}",
                checksum, sourceFile.getFileName());
    }
}