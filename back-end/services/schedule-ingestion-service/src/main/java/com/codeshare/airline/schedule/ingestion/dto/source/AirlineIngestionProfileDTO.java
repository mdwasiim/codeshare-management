package com.codeshare.airline.schedule.ingestion.dto.source;

import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AirlineIngestionProfileDTO extends CSMAuditableDTO {

    private UUID tenantId;
    private String tenantCode;
    private String airlineCode;
    private String sourceSystem;
    private Boolean enabled;
    private Long pollIntervalMs;

    private List<AirlineIngestionChannelDTO> channels;
}
