package com.codeshare.airline.inbound.dto.source;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AirlineIngestionProfileDTO extends CSMAuditableDTO {

    private String airlineCode;
    private String sourceSystem;
    private Boolean enabled;
    private Long pollIntervalMs;

    private List<AirlineIngestionChannelDTO> channels;
}