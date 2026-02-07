package com.codeshare.airline.ingestion.parsing.line;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RawSsimLine {
    private int lineNo;
    private String content; // fixed 80 chars
}
