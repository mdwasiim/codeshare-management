package com.codeshare.airline.core.dto.georegion;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationSearchDTO {

    private UUID id;

    private String code;        // IATA code
    private String name;        // Airport or City name
    private String type;        // AIRPORT or CITY

    private String countryName;
}