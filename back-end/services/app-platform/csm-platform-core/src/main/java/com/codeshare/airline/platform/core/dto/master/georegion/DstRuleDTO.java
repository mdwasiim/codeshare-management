package com.codeshare.airline.platform.core.dto.master.georegion;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DstRuleDTO {

    private Long id;

    private Long timezoneId;
    private String timezoneIdentifier;

    private LocalDateTime dstStart;
    private LocalDateTime dstEnd;

    private Integer dstOffsetMinutes;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    private RecordStatus recordStatus;
}
