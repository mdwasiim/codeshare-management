package com.codeshare.airline.ssim.ingestion.parser;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundCarrier;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.source.SsimTimeMode;
import org.springframework.stereotype.Component;

@Component
public class T2CarrierParser {

    public SsimInboundCarrier parse(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '2') {
            throw new IllegalStateException("Not a T2 record");
        }

        SsimInboundCarrier carrier = new SsimInboundCarrier();
        carrier.setFile(inboundFile);
        carrier.setRecordType("2");

        // Byte-by-byte mapping (example – adjust to your schema)
        carrier.setTimeMode(SsimTimeMode.valueOf(line.substring(1, 2)));           // byte 2
        carrier.setAirlineCode(line.substring(2, 5));        // bytes 3–5
        carrier.setCreationTimeRaw(line.substring(9, 16));     // bytes 10–16
        carrier.setValidityEndRaw(line.substring(16, 23));      // bytes 17–23
        carrier.setCreationDateRaw(line.substring(23, 30));     // bytes 24–30
        carrier.setAirlineName(line.substring(30, 60));      // bytes 31–60
        carrier.setReleaseStatus(line.substring(60, 61));    // byte 61
        carrier.setCreatorReference(line.substring(61, 91)); // bytes 62–91
        carrier.setETicketInfo(line.substring(91, 93));      // bytes 92–93
        carrier.setCreationTimeRaw(line.substring(93, 99));     // bytes 94–99
        carrier.setRecordSerialNumber(line.substring(194, 200)); // bytes 195–200

        carrier.setRawRecord(line);

        return carrier;
    }
}
