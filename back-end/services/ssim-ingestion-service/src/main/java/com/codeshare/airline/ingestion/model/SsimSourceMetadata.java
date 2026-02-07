package com.codeshare.airline.ingestion.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SsimSourceMetadata {
    private String airlineCode;
    private String sender;
    private String location;
}
