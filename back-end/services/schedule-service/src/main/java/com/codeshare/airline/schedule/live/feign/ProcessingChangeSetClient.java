package com.codeshare.airline.schedule.live.feign;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "processing-change-set-client",
        url = "${services.compare.url:${services.processing.url:http://localhost:8089}}"
)
public interface ProcessingChangeSetClient {

    @GetMapping("/schedule/internal/change-sets/{changeSetId}")
    ChangeSetDTO getChangeSet(@PathVariable("changeSetId") UUID changeSetId);
}

