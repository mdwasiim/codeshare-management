package com.codeshare.airline.schedule.compare.feign;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerAcceptanceRuleDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "tenant-acceptance-rule-client",
        url = "${services.tenant.url:http://localhost:8086}"
)
public interface TenantAcceptanceRuleClient {

    @GetMapping("/tenant-partner-acceptance-rules/internal/{tenantCode}/{partnerCode}")
    CodesharePartnerAcceptanceRuleDTO resolve(
            @PathVariable("tenantCode") String tenantCode,
            @PathVariable("partnerCode") String partnerCode,
            @RequestParam("messageType") MessageType messageType
    );
}
