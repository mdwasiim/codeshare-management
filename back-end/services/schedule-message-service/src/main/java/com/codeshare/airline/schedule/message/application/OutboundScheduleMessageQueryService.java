package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
import com.codeshare.airline.schedule.message.domain.repository.OutboundScheduleMessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class OutboundScheduleMessageQueryService {

    private final OutboundScheduleMessageRepository repository;

    public OutboundScheduleMessageQueryService(OutboundScheduleMessageRepository repository) {
        this.repository = repository;
    }

    public OutboundScheduleMessageDTO getOutboundMessage(UUID outboundMessageId) {
        return repository.findByOutboundMessageId(outboundMessageId)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Outbound message not found: " + outboundMessageId));
    }

    private OutboundScheduleMessageDTO toDto(OutboundScheduleMessageEntity entity) {
        return OutboundScheduleMessageDTO.builder()
                .outboundMessageId(entity.getOutboundMessageId())
                .changeSetId(entity.getChangeSetId())
                .changeRequestId(entity.getChangeRequestId())
                .importedScheduleId(entity.getImportedScheduleId())
                .importBatchId(entity.getImportBatchId())
                .messageType(entity.getMessageType())
                .airlineCode(entity.getAirlineCode())
                .partnerCode(entity.getPartnerCode())
                .payload(entity.getPayload())
                .status(entity.getStatus().name())
                .generatedAt(entity.getGeneratedAt())
                .build();
    }
}
