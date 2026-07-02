package com.codeshare.airline.core.dto.master.georegion;

import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.core.enums.common.RecordStatus;
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
public class RegionDTO extends CSMAuditableDTO {
    private UUID id;

    private String regionCode;

    private String regionName;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    // Optional nested
    private List<CountryDTO> countries;
}