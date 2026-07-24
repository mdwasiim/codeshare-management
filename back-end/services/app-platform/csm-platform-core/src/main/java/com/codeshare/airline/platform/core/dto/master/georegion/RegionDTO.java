package com.codeshare.airline.platform.core.dto.master.georegion;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RegionDTO extends CSMAuditableDTO {
    private Long id;

    private String regionCode;

    private String regionName;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    // Optional nested
    private List<CountryDTO> countries;
}