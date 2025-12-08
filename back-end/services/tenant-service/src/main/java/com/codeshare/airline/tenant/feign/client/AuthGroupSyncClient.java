package com.codeshare.airline.tenant.feign.client;


import com.codeshare.airline.common.auth.identity.model.TenantGroupSyncDTO;
import com.codeshare.airline.common.auth.identity.model.TenantGroupUserSyncDTO;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.services.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "auth-identity-service",
        url = "${auth.service.url}",
        configuration = FeignConfig.class
)
public interface AuthGroupSyncClient {

    @PostMapping("/internal/groups/sync")
    ServiceResponse syncGroup(@RequestBody TenantGroupSyncDTO dto);

    @DeleteMapping("/internal/groups/{tenantGroupId}")
    ServiceResponse deleteGroup(@PathVariable UUID tenantGroupId);

    @PostMapping("/internal/group-users/assign")
    ServiceResponse assignUser(@RequestBody TenantGroupUserSyncDTO dto);

    @PostMapping("/internal/group-users/remove")
    ServiceResponse removeUser(@RequestBody TenantGroupUserSyncDTO dto);
}
