package com.codeshare.airline.ingestion.parsing.router;

import com.codeshare.airline.ingestion.exception.UnknownSsimRecordTypeException;
import com.codeshare.airline.ingestion.parsing.line.RawSsimLine;
import com.codeshare.airline.ingestion.parsing.record.*;
import org.springframework.stereotype.Component;

@Component
public class SsimRecordRouter {

    public SsimRecord route(RawSsimLine line) {

        String text = line.getContent();
        char type = text.charAt(0);

        Object payload = switch (type) {
            case '1' -> new R1Record(text);
            case '2' -> new R2Record(text);
            case '3' -> new R3Record(text);
            case '4' -> new R4Record(text);
            case '5' -> new R5Record(text);
            case '6' -> new R6Record(text);
            default -> throw new UnknownSsimRecordTypeException(type, text);
        };

        return new SsimRecord(type,text, payload);

    }
}
