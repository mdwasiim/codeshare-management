package com.codeshare.airline.distribution.engine.feign;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "tenant-distribution-profile-client",
        url = "${services.tenant.url:http://localhost:8086}"
)
public interface TenantDistributionProfileClient {

    @GetMapping("/tenant-partner-distribution-profiles/internal/{tenantCode}/{partnerCode}")
    List<CodesharePartnerDistributionProfileDTO> resolve(
            @PathVariable("tenantCode") String tenantCode,
            @PathVariable("partnerCode") String partnerCode,
            @RequestParam("messageType") MessageType messageType
    );
}
