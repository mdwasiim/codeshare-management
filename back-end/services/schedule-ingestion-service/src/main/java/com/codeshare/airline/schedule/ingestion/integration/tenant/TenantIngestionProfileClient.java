package com.codeshare.airline.schedule.ingestion.integration.tenant;

import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "tenant-ingestion-profile-client",
        url = "${tenant.service.url:http://localhost:8086}"
)
public interface TenantIngestionProfileClient {

    @GetMapping("/tenant-ingestion-profiles/internal/all")
    List<AirlineIngestionProfileDTO> getAllProfiles();
}
