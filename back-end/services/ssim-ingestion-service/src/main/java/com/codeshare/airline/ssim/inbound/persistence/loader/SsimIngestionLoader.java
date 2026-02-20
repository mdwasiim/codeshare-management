package com.codeshare.airline.ssim.inbound.persistence.loader;

import com.codeshare.airline.ssim.inbound.parsing.parser.SsimParser;
import com.codeshare.airline.ssim.inbound.parsing.processor.FlightProcessor;
import com.codeshare.airline.ssim.inbound.parsing.reader.SsimLineReader;
import com.codeshare.airline.ssim.inbound.parsing.splitter.FlightBlockSplitter;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundCarrier;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundHeader;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
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
    private final FlightProcessor flightProcessor;


    private final SsimInboundFileService inboundFileService;

    public void loadStream(InputStream stream,
                           SsimInboundFile inboundFile) {

        try (SsimLineReader reader = new SsimLineReader(stream)) {

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
                                    block -> flightProcessor.process(block, inboundFile)
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
                    block -> flightProcessor.process(block, inboundFile)
            );

        } catch (Exception ex) {
            log.error("SSIM processing failed fileId={}",inboundFile.getId(), ex);
            throw  new IllegalStateException("");
        }
    }
}


