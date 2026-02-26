package com.codeshare.airline.schedule.persistence.ssim.loader;

import com.codeshare.airline.schedule.parsing.ssim.parser.SsimParser;
import com.codeshare.airline.schedule.orchestration.stage.ssim.parsing.SsimFlightBlockProcessor;
import com.codeshare.airline.schedule.parsing.common.reader.LineReader;
import com.codeshare.airline.schedule.parsing.common.splitter.FlightBlockSplitter;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundCarrier;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundHeader;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundTrailer;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimIngestionLoader {

    private final SsimParser ssimParser;
    private final FlightBlockSplitter splitter;
    private final SsimFlightBlockProcessor ssimFlightBlockProcessor;


    private final SsimInboundFileService inboundFileService;

    public void loadStream(InputStream stream,
                           SsimInboundFile inboundFile) {

        try (LineReader reader = new LineReader(stream)) {

            String line;

            while ((line = reader.nextLine()) != null) {

                if (line.isBlank()) continue;

                line = line.replaceAll("^[\\r\\n]+", "");
                char type = line.charAt(0);

                switch (type) {

                    case '1' ->{
                        SsimInboundHeader ssimInboundHeader = ssimParser.processRecord1(line, inboundFile);
                        inboundFileService.saveHeader(ssimInboundHeader);
                    }
                    case '2' -> {
                        SsimInboundCarrier ssimInboundCarrier = ssimParser.processRecord2(line, inboundFile);
                        inboundFileService.saveCarrier(ssimInboundCarrier);
                    }
                    case '3', '4' ->
                            splitter.accept(line,
                                    block -> ssimFlightBlockProcessor.process(block, inboundFile)
                            );
                    case '5' -> {
                        SsimInboundTrailer ssimInboundTrailer = ssimParser.processRecord5(line, inboundFile);
                        inboundFileService.saveTrailer(ssimInboundTrailer);
                    }
                    case '0' -> {
                        log.info("skipping record type 0");
                    }
                    default ->
                            throw new IllegalStateException(
                                    "Unknown record type: " + type);
                }
            }

            splitter.flushRemaining(
                    block -> ssimFlightBlockProcessor.process(block, inboundFile)
            );

        } catch (Exception ex) {
            log.error("SSIM processing failed fileId={}",inboundFile.getId(), ex);
            throw  new IllegalStateException("");
        }
    }
}


