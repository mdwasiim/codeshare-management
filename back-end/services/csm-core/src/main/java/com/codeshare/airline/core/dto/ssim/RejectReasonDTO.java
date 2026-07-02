package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class RejectReasonDTO {

    private UUID id;
    private String rejectReasonCode;
    private String rejectReasonName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
