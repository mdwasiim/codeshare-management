package com.codeshare.airline.core.dto.codeshare;

import com.codeshare.airline.core.enums.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeshareRouteDTO {

    private UUID id;

    private UUID agreementId;

    private UUID originId;
    private UUID destinationId;

    private Boolean bidirectional;

    private Status statusCode;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // Optional response-only fields
    private String originCode;
    private String destinationCode;
}