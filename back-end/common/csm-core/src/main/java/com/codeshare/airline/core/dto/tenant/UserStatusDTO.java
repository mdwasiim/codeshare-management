package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusDTO {

    private UUID tenantId;
    private String userId;
    private RecordStatus recordStatus;
}
