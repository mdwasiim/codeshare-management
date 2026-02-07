package com.codeshare.airline.processor.processing.parsing;

import com.codeshare.airline.processor.pipeline.dto.SsimR3FlightLegRecordDTO;
import com.codeshare.airline.processor.pipeline.dto.SsimR3SegmentDataRecordDTO;
import com.codeshare.airline.processor.pipeline.dto.SsimR4DateVariationRecordDTO;
import com.codeshare.airline.processor.pipeline.model.ParsedSsimResult;
import com.codeshare.airline.processor.pipeline.model.SsimRawFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordRouter {

    private final R1HeaderParser r1Parser;
    private final R2CarrierParser r2Parser;
    private final R3FlightLegParser r3Parser;
    private final R3SegmentDataParser r3SegmentParser;
    private final R4DateVariationParser r4Parser;
    private final R5ContinuationParser r5Parser;

    /**
     * SSIM is a stateful, order-dependent format.
     * This router maintains R3 → R4 → R5 context explicitly.
     */
    public ParsedSsimResult route(SsimRawFile rawFile) {

        ParsedSsimResult result = new ParsedSsimResult(rawFile);

        SsimR3FlightLegRecordDTO lastR3 = null;
        SsimR4DateVariationRecordDTO lastR4 = null;

        int segmentSeq = 1;

        for (var line : rawFile.getLines()) {

            String content = line.getContent();
            if (content == null || content.isEmpty()) {
                result.addUnknown("");
                continue;
            }

            char recordType = content.charAt(0);

            switch (recordType) {

                case '1' -> {
                    result.setR1Header(r1Parser.parse(line));
                }

                case '2' -> {
                    result.addR2(r2Parser.parse(line));
                }

                case '3' -> {
                    lastR3 = r3Parser.parse(line);
                    result.addR3(lastR3);

                    // reset per-flight context
                    lastR4 = null;
                    segmentSeq = 1;
                }

                case '4' -> {
                    if (lastR3 == null) {
                        result.addError("R3 segment data without preceding R3: " + content);
                        break;
                    }

                    SsimR3SegmentDataRecordDTO dto = r3SegmentParser.parse(line);
                    dto.setSequenceNumber(segmentSeq++);
                    result.addR3Segment(dto);
                }

                case '5' -> {
                    if (lastR3 == null) {
                        result.addError("R4 date variation without preceding R3: " + content);
                        break;
                    }

                    lastR4 = r4Parser.parse(line);
                    result.addR4(lastR4);
                }

                case '6' -> {
                    if (lastR4 == null) {
                        result.addError("R5 continuation without preceding R4: " + content);
                    } else {
                        var r5 = r5Parser.parse(line);
                        lastR4.addContinuation(r5);
                        result.addR5(r5); // flat storage for diagnostics/persistence
                    }
                }

                default -> {
                    result.addUnknown(content);
                }
            }
        }

        return result;
    }
}

