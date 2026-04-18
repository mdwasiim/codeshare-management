package com.codeshare.airline.inbound.services.error;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.entities.error.ScheduleErrorEntity;
import com.codeshare.airline.inbound.repositories.error.ScheduleErrorRepository;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsmErrorPersistenceStrategy implements ErrorPersistenceStrategy {

    private final ScheduleErrorRepository repository;

    @Override
    public MessageType getSupportedType() {
        return MessageType.ASM;
    }

    @Override
    public void persist(AbstractIngestionContext<?, ?> context,
                        ValidationStage stage,
                        List<ValidationMessage> messages) {

        if (!(context instanceof AsmIngestionContext ctx)) {
            throw new IllegalArgumentException("Invalid context type for SSM error persistence");
        }

        if (messages == null || messages.isEmpty()) {
            return;
        }

        log.debug("Persisting {} SSM validation errors | fileId={}",
                messages.size(),
                ctx.getMetadata().getFileId());

        Instant now = Instant.now();

        List<ScheduleErrorEntity> entities = messages.stream()
                .map(msg -> buildEntity(ctx, stage, msg, now))
                .toList();

        repository.saveAll(entities);
    }

    private ScheduleErrorEntity buildEntity(AsmIngestionContext ctx,
                                            ValidationStage stage,
                                            ValidationMessage msg,
                                            Instant timestamp) {

        ScheduleErrorEntity e = new ScheduleErrorEntity();

        e.setFileId(ctx.getMetadata().getFileId());
        e.setLoadId(ctx.getMetadata().getLoadId());
        e.setMessageType(ctx.getMetadata().getMessageType());

        e.setRuleCode(msg.getRuleCode());
        e.setMessage(msg.getMessage());
        e.setSeverity(msg.getSeverity());

        e.setValidationStage(stage);
        e.setValidatedAt(timestamp);

        return e;
    }
}