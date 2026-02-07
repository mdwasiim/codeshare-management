package com.codeshare.airline.processor.processing.parsing;


import com.codeshare.airline.processor.pipeline.dto.SsimR1HeaderRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;

@Component
public class R1HeaderParser implements RecordParser<SsimR1HeaderRecordDTO> {

    @Override
    public SsimR1HeaderRecordDTO parse(SsimRawLine ssimRawLine) {

        String line = ssimRawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM R1 line length");
        }

        if (line.charAt(0) != '1') {
            throw new IllegalArgumentException("Not an SSIM Record-1 line");
        }

        return SsimR1HeaderRecordDTO.builder()
                .airlineDesignator(substring(line, 2, 4))          // Bytes 2–4
                .datasetSerialNumber(substring(line, 6, 11))       // Bytes 6–11
                .creationDate(substring(line, 13, 18))             // Bytes 13–18 (YYMMDD)
                .scheduleType(substring(line, 25, 25))             // Byte 25
                .periodStartDate(substring(line, 26, 31))          // Bytes 26–31
                .periodEndDate(substring(line, 33, 38))            // Bytes 33–38
                .versionNumber(substring(line, 40, 41))            // Bytes 40–41
                .continuationIndicator(substring(line, 43, 43))    // Byte 43
                .generalInformation(substring(line, 44, 78))       // Bytes 44–78
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

