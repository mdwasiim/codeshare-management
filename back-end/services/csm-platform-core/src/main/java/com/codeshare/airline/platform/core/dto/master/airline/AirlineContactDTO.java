package com.codeshare.airline.platform.core.dto.master.airline;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineContactType;
import com.codeshare.airline.platform.core.enums.master.airline.CommunicationMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AirlineContactDTO {
    private Long id;
    private Long airlineId;
    private String contactCode;
    private String contactName;
    private String designation;
    private String department;
    private AirlineContactType contactType;
    private String email;
    private String phone;
    private String mobile;
    private String fax;
    private CommunicationMethod preferredCommunication;
    private Long timeZoneId;
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
