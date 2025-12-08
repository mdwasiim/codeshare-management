package com.codeshare.airline.auth.feign.client;

import com.codeshare.airline.common.tenant.model.TenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "tenant-service", path = "/internal/tenants")
public interface TenantFeignClient {

    @GetMapping
    List<TenantDTO> getAllTenants();
}
