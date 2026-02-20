package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.core.enums.common.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CountryDTO extends CSMAuditableDTO {

    private UUID id;
    private String iso2Code;
    private String iso3Code;
    private String countryName;

    private Status statusCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // Many-to-One (Lightweight Parent)
    private RegionDTO region;

    // One-to-Many
    private List<StateDTO> states;
}