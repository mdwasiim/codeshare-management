package com.codeshare.airline.schedule.validation.persistence.service;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.persistence.entity.ScheduleInboundValidation;
import com.codeshare.airline.schedule.validation.persistence.repository.ScheduleInboundValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleValidationPersistenceService {

    private final ScheduleInboundValidationRepository repository;

    public void persistValidationResults(
            UUID fileId,
            ScheduleMessageType messageType,
            String stage,
            List<ValidationMessage> messages) {

        for (ValidationMessage msg : messages) {

            ScheduleInboundValidation entity =
                    new ScheduleInboundValidation();

            entity.setFileId(fileId);
            entity.setMessageType(messageType);
            entity.setErrorCode(msg.getCode());
            entity.setErrorMessage(msg.getMessage());
            entity.setSeverity(msg.getSeverity());
            entity.setBlocking(msg.isBlocking());
            entity.setValidationStage(stage);
            entity.setValidatedAt(Instant.now());

            repository.save(entity);
        }
    }
}