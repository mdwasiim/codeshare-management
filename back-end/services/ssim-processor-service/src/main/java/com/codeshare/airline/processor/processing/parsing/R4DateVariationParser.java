package com.codeshare.airline.processor.processing.parsing;


import com.codeshare.airline.processor.pipeline.dto.SsimR4DateVariationRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;

@Component
public class R4DateVariationParser implements RecordParser<SsimR4DateVariationRecordDTO> {

    @Override
    public SsimR4DateVariationRecordDTO parse(SsimRawLine ssimRawLine) {

        String line = ssimRawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM Record-5 line length");
        }

        if (line.charAt(0) != '5') {
            throw new IllegalArgumentException("Not an SSIM Record-5 (Date Variation) line");
        }

        return SsimR4DateVariationRecordDTO.builder()
                .variationDate(substring(line, 10, 15))           // YYMMDD
                .actionCode(substring(line, 16, 16))              // A / C / D
                .dayChangeIndicator(substring(line, 17, 17))      // + / -
                .scheduledDepartureTime(substring(line, 18, 21)) // STD override
                .scheduledArrivalTime(substring(line, 22, 25))   // STA override
                .aircraftType(substring(line, 26, 28))            // Aircraft override
                .trafficRestrictionCode(substring(line, 29, 30)) // TRC override
                .reserved(substring(line, 31, 31))                // Reserved byte
                .remarks(substring(line, 32, 78))                 // Free text
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

