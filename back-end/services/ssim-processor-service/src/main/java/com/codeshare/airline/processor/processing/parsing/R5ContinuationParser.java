package com.codeshare.airline.processor.parsing;

import com.codeshare.airline.processor.pipeline.dto.SsimR5ContinuationRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;

@Component
public class R5ContinuationParser implements RecordParser<SsimR5ContinuationRecordDTO> {

    @Override
    public SsimR5ContinuationRecordDTO parse(SsimRawLine rawLine) {

        String line = rawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM Record-6 line length");
        }

        if (line.charAt(0) != '6') {
            throw new IllegalArgumentException("Not an SSIM Record-6 (Continuation) line");
        }

        return SsimR5ContinuationRecordDTO.builder()
                .continuationData(substring(line, 2, 78)) // raw continuation payload
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
