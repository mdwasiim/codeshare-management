package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusDTO {

    private Long tenantId;
    private String userId;
    private RecordStatus recordStatus;
}
