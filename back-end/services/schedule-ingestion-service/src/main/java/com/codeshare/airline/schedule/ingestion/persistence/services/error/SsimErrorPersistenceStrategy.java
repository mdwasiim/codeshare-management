package com.codeshare.airline.schedule.ingestion.persistence.services.error;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.persistence.entities.error.SsimErrorEntity;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.error.SsimErrorRepository;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
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
                    e.setRecordType(recordType(msg.getRecordType()));
                    e.setRecordKey(truncate(msg.getRecordKey(), 200));

                    e.setSeverity(msg.getSeverity());
                    e.setRuleCode(msg.getRuleCode());
                    e.setMessage(msg.getMessage());

                    e.setValidationStage(stage);

                    return e;

                }).toList();

        repository.saveAll(entities);
    }

    private String recordType(String value) {
        if (value == null || value.isBlank()) {
            return "NA";
        }

        String trimmed = value.trim();
        if (trimmed.length() <= 2) {
            return trimmed;
        }

        char first = trimmed.charAt(0);
        if (first >= '1' && first <= '5') {
            return "T" + first;
        }

        return trimmed.substring(0, 2);
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
