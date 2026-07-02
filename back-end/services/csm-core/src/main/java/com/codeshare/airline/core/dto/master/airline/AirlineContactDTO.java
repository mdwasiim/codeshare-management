package com.codeshare.airline.core.dto.master.airline;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AirlineContactDTO {
    private UUID id;
    private UUID airlineId;
    private String contactCode;
    private String contactName;
    private String designation;
    private String department;
    private String contactType;
    private String email;
    private String phone;
    private String mobile;
    private String fax;
    private String preferredCommunication;
    private UUID languageId;
    private UUID timeZoneId;
    private Boolean available24x7;
    private Boolean emergencyContact;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
