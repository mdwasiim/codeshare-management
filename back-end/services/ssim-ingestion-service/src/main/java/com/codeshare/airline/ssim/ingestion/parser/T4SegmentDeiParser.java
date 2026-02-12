package com.codeshare.airline.ssim.ingestion.parser;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundSegmentDei;
import org.springframework.stereotype.Component;

@Component
public class T4SegmentDeiParser {

    public SsimInboundSegmentDei parse(
            String line,
            SsimInboundFlightLeg flightLeg
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '4') {
            throw new IllegalStateException("Not a T4 record");
        }

        SsimInboundSegmentDei dei = new SsimInboundSegmentDei();
        dei.setFlightLeg(flightLeg);
        dei.setRecordType("4");

        // ---- Core identifiers ----
        dei.setOperationalVariation(line.substring(1, 2));    // byte 2
        dei.setAirlineCode(line.substring(2, 5).trim());             // bytes 3–5
        dei.setFlightNumber(line.substring(5, 9).trim());           // bytes 6–9
        dei.setOperationalSuffix(line.substring(9, 11).trim());     // bytes 10–11

        // ---- Segment points ----
        dei.setBoardPoint(line.substring(11, 14).trim());           // bytes 12–14
        dei.setOffPoint(line.substring(14, 17).trim());             // bytes 15–17

        // ---- DEI metadata ----
        dei.setDeiNumber(line.substring(17, 20));             // bytes 18–20

        // ---- DEI payload ----
        dei.setDeiPayload(line.substring(20, 194));           // bytes 21–194

        // ---- Footer ----
        dei.setRecordSerialNumber(line.substring(194, 200));  // bytes 195–200

        dei.setRawRecord(line);

        return dei;
    }
}
