package com.codeshare.airline.processor.processing.parsing;


import com.codeshare.airline.processor.pipeline.dto.SsimR3SegmentDataRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;

@Component
public class R3SegmentDataParser implements RecordParser<SsimR3SegmentDataRecordDTO> {

    @Override
    public SsimR3SegmentDataRecordDTO parse(SsimRawLine ssimRawLine) {

        String line = ssimRawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM Record-4 line length");
        }

        if (line.charAt(0) != '4') {
            throw new IllegalArgumentException("Not an SSIM Record-4 (Segment Data) line");
        }

        return SsimR3SegmentDataRecordDTO.builder()
                .boardPoint(substring(line, 10, 12))          // Bytes 10–12
                .offPoint(substring(line, 13, 15))            // Bytes 13–15
                .dataElementIdentifier(substring(line, 16, 18)) // Bytes 16–18
                .dataElementValue(substring(line, 19, 78))    // Bytes 19–78 (60 chars)
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
