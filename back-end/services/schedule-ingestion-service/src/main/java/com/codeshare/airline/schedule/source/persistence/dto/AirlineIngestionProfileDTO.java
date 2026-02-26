package com.codeshare.airline.schedule.source.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AirlineIngestionProfileDTO {

    private UUID id;

    private String airlineCode;
    private String sourceSystem;
    private Boolean enabled;
    private Long pollIntervalMs;

    private List<AirlineIngestionChannelDTO> channels;
}