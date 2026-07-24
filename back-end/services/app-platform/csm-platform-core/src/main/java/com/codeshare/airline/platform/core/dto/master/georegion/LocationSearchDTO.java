package com.codeshare.airline.platform.core.dto.master.georegion;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationSearchDTO {

    private Long id;

    private String code;        // IATA code
    private String name;        // Airport or City name
    private String type;        // AIRPORT or CITY

    private String countryName;
}