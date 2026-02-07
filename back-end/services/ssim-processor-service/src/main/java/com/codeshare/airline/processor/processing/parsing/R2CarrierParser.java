package com.codeshare.airline.processor.processing.parsing;

import com.codeshare.airline.processor.pipeline.dto.SsimR2CarrierRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;

@Component
public class R2CarrierParser implements RecordParser<SsimR2CarrierRecordDTO> {

    @Override
    public SsimR2CarrierRecordDTO parse(SsimRawLine ssimRawLine) {

        String line = ssimRawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM R2 line length");
        }

        if (line.charAt(0) != '2') {
            throw new IllegalArgumentException("Not an SSIM Record-2 line");
        }

        return SsimR2CarrierRecordDTO.builder()
                .airlineDesignator(substring(line, 2, 4))          // Bytes 2–4
                .airlineNumericCode(substring(line, 5, 7))         // Bytes 5–7
                .airlineName(substring(line, 8, 27))               // Bytes 8–27
                .countryCode(substring(line, 28, 29))              // Bytes 28–29
                .currencyCode(substring(line, 30, 31))             // Bytes 30–31
                .icaoDesignator(substring(line, 32, 36))           // Bytes 32–36
                .duplicateDesignatorMarker(substring(line, 37, 37))// Byte 37
                .remarks(substring(line, 38, 78))                  // Bytes 38–78
                .build();
    }

    /**
     * SSIM positions are 1-based and inclusive.
     * DO NOT trim or normalize.
     */
    private String substring(String line, int start, int end) {
        return line.substring(start - 1, end);
    }
}
