package com.codeshare.airline.ssim.ingestion.loader;

import com.codeshare.airline.ssim.ingestion.contex.FlightContext;
import com.codeshare.airline.ssim.ingestion.contex.SsimLoadContext;
import com.codeshare.airline.ssim.ingestion.parser.*;
import com.codeshare.airline.ssim.ingestion.reader.SsimLineReader;
import com.codeshare.airline.ssim.ingestion.service.SsimInboundFileService;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundCarrier;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundHeader;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.processor.service.SsimPersistenceService;
import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.engine.SsimValidationEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimIngestionLoader {

    private final T1HeaderParser t1Parser;
    private final T2CarrierParser t2Parser;
    private final T3FlightLegParser t3Parser;
    private final T4SegmentDeiParser t4Parser;
    private final T5TrailerParser t5Parser;

    private final SsimPersistenceService persistenceService;
    private final SsimInboundFileService inboundFileService;
    private final SsimValidationEngine validationEngine;

    public void load(InputStream inputStream, SsimInboundFile inboundFile) {

        SsimLoadContext loadCtx = new SsimLoadContext();
        loadCtx.setInboundFile(inboundFile);

        ValidationContext validationCtx = new ValidationContext();
        FlightContext current = null;

        try (SsimLineReader reader = new SsimLineReader(inputStream)) {

            String line;

            while ((line = reader.nextLine()) != null) {

                loadCtx.setTotalRecordsRead( loadCtx.getTotalRecordsRead() + 1);

                // 🔥 STRUCTURAL VALIDATION (length + order)
                validationEngine.validateRecord(line, validationCtx);

                char recordType = line.charAt(0);

                switch (recordType) {

                    case '1' -> {
                        if (loadCtx.isT1Seen())
                            throw new IllegalStateException("Duplicate T1");

                        SsimInboundHeader header =t1Parser.parse(line, inboundFile);
                        inboundFileService.saveHeader(header);
                        loadCtx.setT1Seen(true);
                    }

                    case '2' -> {
                        if (!loadCtx.isT1Seen())
                            throw new IllegalStateException("T2 before T1");
                        if (loadCtx.isT2Seen())
                            throw new IllegalStateException("Duplicate T2");

                        SsimInboundCarrier carrier =t2Parser.parse(line, inboundFile);
                        inboundFileService.saveCarrier(carrier);
                        loadCtx.setT2Seen(true);
                    }

                    case '3' -> {
                        if (!loadCtx.isT2Seen())
                            throw new IllegalStateException("T3 before T2");

                        flushFlight(current, loadCtx);
                        current = FlightContext.builder().build();
                        current.setFlight(t3Parser.parse(line, inboundFile) );
                    }


                    case '4' -> {
                        if (current == null || current.getFlight() == null)
                            throw new IllegalStateException("T4 without T3");

                        validationEngine.validateFlightTransition(current, validationCtx);
                        current.getDeis().add(t4Parser.parse(line, current.getFlight()));
                    }
                    case '5' -> {
                        flushFlight(current, loadCtx);
                        current = null;

                        SsimInboundTrailer trailer = t5Parser.parse(line, inboundFile);
                        // 🔥 TRAILER VALIDATION
                        validationEngine.validateTrailer(trailer,validationCtx);
                        inboundFileService.saveTrailer(trailer);

                        loadCtx.setT5Seen(true);
                    }

                    default ->
                            throw new IllegalStateException("Unknown record type: " + recordType);
                }
            }

            flushFlight(current, loadCtx);

            if (!validationCtx.getWarnings().isEmpty()) {
                log.warn( "SSIM file {} ingested with {} warnings", inboundFile.getFileId(), validationCtx.getWarnings().size());
            }

        } catch (Exception ex) {
            inboundFileService.updateStatus(inboundFile.getFileId(), SsimProcessingStatus.PARTIAL);
            log.error("SSIM ingestion failed for fileId={}, recordsRead={}",inboundFile.getFileId(), loadCtx.getTotalRecordsRead(),ex);
        }
    }

    private void flushFlight(FlightContext ctx,SsimLoadContext loadCtx    ) {
        if (ctx == null || ctx.getFlight() == null) {
            return;
        }
        persistenceService.persist(ctx.getFlight(),ctx.getDeis());
        loadCtx.setTotalFlightsPersisted( loadCtx.getTotalFlightsPersisted() + 1);
    }
}
