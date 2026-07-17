package com.codeshare.airline.distribution.engine.feign;

import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "distribution-outbound-message-client",
        url = "${services.message.url:http://localhost:8090}"
)
public interface OutboundScheduleMessageClient {

    @GetMapping("/schedule/internal/outbound-messages/{outboundMessageId}")
    OutboundScheduleMessageDTO getOutboundMessage(@PathVariable("outboundMessageId") UUID outboundMessageId);
}
