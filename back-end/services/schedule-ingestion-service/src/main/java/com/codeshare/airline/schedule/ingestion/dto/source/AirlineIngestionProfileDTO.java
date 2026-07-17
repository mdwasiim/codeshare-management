package com.codeshare.airline.schedule.ingestion.dto.source;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AirlineIngestionProfileDTO extends CSMAuditableDTO {

    private Long tenantId;
    private String tenantCode;
    private String airlineCode;
    private String sourceSystem;
    private Boolean enabled;
    private Long pollIntervalMs;

    private List<AirlineIngestionChannelDTO> channels;
}
