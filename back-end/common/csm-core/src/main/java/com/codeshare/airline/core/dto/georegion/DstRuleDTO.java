package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.enums.common.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DstRuleDTO {

    private UUID id;

    private UUID timezoneId;

    private LocalDateTime dstStart;
    private LocalDateTime dstEnd;

    private Integer dstOffsetMinutes;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    private Status statusCode;
}