package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.enums.UserStatus;
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
    private UserStatus status;
}
