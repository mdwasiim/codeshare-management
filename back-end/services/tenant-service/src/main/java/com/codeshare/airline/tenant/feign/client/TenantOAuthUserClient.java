package com.codeshare.airline.tenant.feign.client;


import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.tenant.common.TenantFeignWebContextConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "authentication-identity-service",
        configuration = TenantFeignWebContextConfiguration.class
)
public interface TenantOAuthUserClient {

    @GetMapping("/internal/users")
    List<AuthUserDTO> getAllUsers();
}
