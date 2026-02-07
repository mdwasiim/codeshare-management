package com.codeshare.airline.ingestion.event.model;

import com.codeshare.airline.ingestion.parsing.record.SsimRecord;
import com.codeshare.airline.ingestion.source.SourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class ParsedSsimBatchEvent {

    private String fileId;
    private SourceType sourceType;
    private Instant receivedAt;

    private List<SsimRecord> records;
}
