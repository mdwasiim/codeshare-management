package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.enums.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SeasonDTO {

    private UUID id;
    private String seasonCode;
    private String seasonName;
    private Status status;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}