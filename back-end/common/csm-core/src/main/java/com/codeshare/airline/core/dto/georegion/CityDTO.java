package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    private List<AirportDTO> airports;
}