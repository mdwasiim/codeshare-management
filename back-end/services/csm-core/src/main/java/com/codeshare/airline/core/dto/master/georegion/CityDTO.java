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
public class CityDTO extends CSMAuditableDTO {

    private UUID id;
    private String name;
    private String iataCode;

    private UUID stateId;
    private StateDTO state;

    private UUID countryId;
    private CountryDTO country;

    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private List<AirportDTO> airports;
}
