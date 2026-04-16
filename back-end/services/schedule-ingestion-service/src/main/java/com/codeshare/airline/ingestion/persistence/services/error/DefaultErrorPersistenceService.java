package com.codeshare.airline.ingestion.persistence.services.error;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.validations.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DefaultErrorPersistenceService implements ErrorPersistenceService {

    private final List<ErrorPersistenceStrategy> strategies;

    // 🔥 cache by MessageType (better than Class)
    private final Map<MessageType, ErrorPersistenceStrategy> strategyCache = new ConcurrentHashMap<>();

    @Override
    public void persist(AbstractIngestionContext<?, ?> context,ValidationStage stage, List<ValidationMessage> messages) {

        if (messages == null || messages.isEmpty()) {
            return;
        }

        ErrorPersistenceStrategy strategy = resolveStrategy(context);

        strategy.persist(context, stage, messages);
    }

    private ErrorPersistenceStrategy resolveStrategy(AbstractIngestionContext<?, ?> context) {

        MessageType type = context.getMessageType();

        return strategyCache.computeIfAbsent(type, t ->
                strategies.stream()
                        .filter(s -> s.getSupportedType() == t)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "No ErrorPersistenceStrategy for type: " + t))
        );
    }
}