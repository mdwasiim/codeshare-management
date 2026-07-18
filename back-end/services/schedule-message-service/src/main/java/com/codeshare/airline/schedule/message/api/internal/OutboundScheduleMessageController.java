package com.codeshare.airline.schedule.message.api.internal;

import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.schedule.message.application.OutboundScheduleMessageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping({"/schedule/internal/outbound-messages", "/internal/outbound-messages"})
@RequiredArgsConstructor
public class OutboundScheduleMessageController {

    private final OutboundScheduleMessageQueryService queryService;

    @GetMapping("/{outboundMessageId}")
    public OutboundScheduleMessageDTO getOutboundMessage(@PathVariable UUID outboundMessageId) {
        return queryService.getOutboundMessage(outboundMessageId);
    }
}
