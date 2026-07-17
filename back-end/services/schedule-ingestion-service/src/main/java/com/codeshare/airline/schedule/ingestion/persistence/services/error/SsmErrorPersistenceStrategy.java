package com.codeshare.airline.schedule.ingestion.persistence.services.error;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.persistence.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.error.ScheduleErrorRepository;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SsmErrorPersistenceStrategy implements ErrorPersistenceStrategy {

    private final ScheduleErrorRepository repository;

    @Override
    public MessageType getSupportedType() {
        return MessageType.SSM;
    }

    @Override
    public void persist(AbstractIngestionContext<?, ?> context,
                        ValidationStage stage,
                        List<ValidationMessage> messages) {

        if (!(context instanceof SsmIngestionContext ctx)) {
            throw new IllegalArgumentException("Invalid context type for SSM error persistence");
        }

        if (messages == null || messages.isEmpty()) {
            return;
        }

        log.debug("Persisting {} SSM validation errors | fileId={}",
                messages.size(),
                ctx.getMetadata().getFileId());

        List<ScheduleErrorEntity> entities = messages.stream()
                .map(msg -> buildEntity(ctx, stage, msg))
                .toList();

        repository.saveAll(entities);
    }

    private ScheduleErrorEntity buildEntity(SsmIngestionContext ctx,
                                            ValidationStage stage,
                                            ValidationMessage msg) {

        ScheduleErrorEntity e = new ScheduleErrorEntity();

        e.setFileId(ctx.getMetadata().getFileId());
        e.setLoadId(ctx.getMetadata().getLoadId());
        e.setMessageType(ctx.getMetadata().getMessageType());
        e.setRecordType(truncate(msg.getRecordType(), 10));
        e.setRecordKey(truncate(msg.getRecordKey(), 200));

        e.setRuleCode(msg.getRuleCode());
        e.setMessage(msg.getMessage());
        e.setSeverity(msg.getSeverity());

        e.setValidationStage(resolveStage(stage, msg));
        e.setValidatedAt(Instant.now());

        return e;
    }

    private ValidationStage resolveStage(ValidationStage fallbackStage, ValidationMessage message) {
        return message.getStage() != null ? message.getStage() : fallbackStage;
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
