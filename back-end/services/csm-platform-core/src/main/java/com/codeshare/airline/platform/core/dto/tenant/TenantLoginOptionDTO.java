package com.codeshare.airline.platform.core.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantLoginOptionDTO {

    private Long id;
    private String name;
    private String tenantCode;
    private String logoUrl;
}
