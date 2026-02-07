package com.codeshare.airline.ingestion.model;

import com.codeshare.airline.ingestion.source.SourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class SsimRawFile {

    private String fileId;
    private String originalFileName;
    private byte[] content;

    private SourceType sourceType;
    private Instant receivedAt;

    private SsimSourceMetadata metadata;
}
