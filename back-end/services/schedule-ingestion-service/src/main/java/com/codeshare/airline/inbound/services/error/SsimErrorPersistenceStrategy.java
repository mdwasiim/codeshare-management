package com.codeshare.airline.inbound.services.error;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.entities.error.SsimErrorEntity;
import com.codeshare.airline.inbound.repositories.error.SsimErrorRepository;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class SsimErrorPersistenceStrategy implements ErrorPersistenceStrategy {

    private final SsimErrorRepository repository;

    @Override
    public MessageType getSupportedType() {
        return MessageType.SSIM;
    }

    @Override
    public void persist(AbstractIngestionContext<?, ?> context,
                        ValidationStage stage,
                        List<ValidationMessage> messages) {

        SsimIngestionContext ctx = (SsimIngestionContext) context;

        List<SsimErrorEntity> entities = messages.stream()
                .map(msg -> {

                    SsimErrorEntity e = new SsimErrorEntity();

                    e.setFileId(ctx.getMetadata().getFileId());
                    e.setLoadId(ctx.getMetadata().getLoadId());
                    e.setRecordType(msg.getRecordType());
                    e.setRecordKey(msg.getRecordKey());

                    e.setSeverity(msg.getSeverity());
                    e.setRuleCode(msg.getRuleCode());
                    e.setMessage(msg.getMessage());

                    e.setValidationStage(stage);

                    return e;

                }).toList();

        repository.saveAll(entities);
    }
}