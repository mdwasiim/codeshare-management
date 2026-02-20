package com.codeshare.airline.ssim.inbound.parsing.processor;

import com.codeshare.airline.ssim.inbound.domain.contex.FlightBlockContext;
import com.codeshare.airline.ssim.inbound.parsing.parser.SsimParser;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.ParsedFlightDTO;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightProcessor {

    private final SsimParser ssimParser;
    private final SsimPersistenceService persistenceService;

    public void process(FlightBlockContext block, SsimInboundFile inboundFile) {

        ParsedFlightDTO parsed = parseFlight(block, inboundFile);

        persistenceService.persist(parsed.getLegData(), parsed.getSegments());
    }

    private ParsedFlightDTO parseFlight(FlightBlockContext block, SsimInboundFile inboundFile) {

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

        return ParsedFlightDTO.builder()
                .carrier(block.getCarrier())
                .flightNumber(block.getFlightNumber())
                .legData(leg)
                .segments(segments)
                .build();
    }
}
