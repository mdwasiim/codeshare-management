package com.codeshare.airline.distribution.engine.feign;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "tenant-communication-profile-client",
        url = "${services.tenant.url:http://localhost:8086}"
)
public interface TenantCommunicationProfileClient {

    @GetMapping("/tenant-partner-communication-profiles/internal/{tenantCode}/{partnerCode}")
    List<CodesharePartnerCommunicationProfileDTO> resolve(
            @PathVariable("tenantCode") String tenantCode,
            @PathVariable("partnerCode") String partnerCode,
            @RequestParam("protocol") CommunicationProtocol protocol
    );
}
