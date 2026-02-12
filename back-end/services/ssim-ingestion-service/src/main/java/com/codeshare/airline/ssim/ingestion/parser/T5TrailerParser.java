package com.codeshare.airline.ssim.ingestion.parser;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import org.springframework.stereotype.Component;

@Component
public class T5TrailerParser {

    public SsimInboundTrailer parse(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '5') {
            throw new IllegalStateException("Not a T5 record");
        }

        SsimInboundTrailer trailer = new SsimInboundTrailer();
        trailer.setFile(inboundFile);
        trailer.setRecordType("5");

        // ---- Trailer fields ----
        trailer.setTrailerTitle(line.substring(1, 36));        // bytes 2–36

        String totalCountRaw = line.substring(36, 45);// bytes 37–45
        trailer.setTotalRecordCountRaw(totalCountRaw);
        try {
            trailer.setTotalRecordCount(
                    Integer.parseInt(totalCountRaw.trim())
            );
        } catch (NumberFormatException ex) {
            throw new IllegalStateException(
                    "Invalid total record count in T5: " + totalCountRaw
            );
        }
        trailer.setSpare46To194(line.substring(45, 194));             // bytes 46–194
        trailer.setRecordSerialNumber(line.substring(194, 200)); // bytes 195–200

        trailer.setRawRecord(line);

        return trailer;
    }
}
