package com.codeshare.airline.schedule.persistence.ssim.loader;

import com.codeshare.airline.schedule.parsing.common.splitter.FlightBlockContext;
import com.codeshare.airline.schedule.parsing.ssim.parser.SsimParser;
import com.codeshare.airline.schedule.parsing.ssim.dto.ParsedFlight;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SsimFlightBlockProcessor {

    private final SsimParser ssimParser;
    private final SsimPersistenceService persistenceService;

    public void process(FlightBlockContext block, SsimInboundFile inboundFile) {

        ParsedFlight parsed = parseFlight(block, inboundFile);

        persistenceService.persist(parsed.getLegData(), parsed.getSegments());
    }

    private ParsedFlight parseFlight(FlightBlockContext block, SsimInboundFile inboundFile) {

        List<String> lines = block.getLines();

        if (lines.isEmpty()) {
            throw new IllegalStateException("Empty flight block");
        }

        // 🔵 First line must be T3
        String t3Line = lines.get(0);

        SsimInboundFlightLeg leg = ssimParser.processRecord3(t3Line, inboundFile);

        // 🔵 Remaining lines are T4
        List<SsimInboundSegmentDei> segments = lines.stream()
                .skip(1)
                .map(line -> ssimParser.processRecord4(line, leg))
                .toList();

        return ParsedFlight.builder()
                .carrier(block.getCarrier())
                .flightNumber(block.getFlightNumber())
                .legData(leg)
                .segments(segments)
                .build();
    }
}
