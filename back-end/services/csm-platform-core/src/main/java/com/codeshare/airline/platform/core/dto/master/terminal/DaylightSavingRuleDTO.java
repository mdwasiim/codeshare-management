package com.codeshare.airline.platform.core.dto.master.terminal;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DaylightSavingRuleDTO {

    private UUID id;
    private String ruleCode;
    private String ruleName;
    private Integer dstOffsetMinutes;
    private String startRule;
    private String endRule;
    private String description;
    private UUID timezoneId;
    private String timezoneIdentifier;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
