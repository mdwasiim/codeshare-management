package com.codeshare.airline.platform.core.dto.master.airline;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AllianceMembershipStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AllianceMembershipType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AllianceMemberDTO {
    private Long id;
    private Long allianceId;
    private Long airlineId;
    private AllianceMembershipType membershipType;
    private AllianceMembershipStatus membershipStatus;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private Boolean primaryMember;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
