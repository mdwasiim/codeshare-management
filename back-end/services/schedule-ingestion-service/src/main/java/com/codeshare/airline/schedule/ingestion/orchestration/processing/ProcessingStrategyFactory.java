package com.codeshare.airline.schedule.ingestion.orchestration.processing;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProcessingStrategyFactory {

    private final List<ProcessingStrategy<?>> strategies;

    private final Map<MessageType, ProcessingStrategy<?>> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (ProcessingStrategy<?> strategy : strategies) {

            strategyMap.put(strategy.getMessageType(), strategy);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractIngestionContext<?, ?>> ProcessingStrategy<T> get(
            AbstractIngestionContext<?, ?> context
    ) {
        ProcessingStrategy<?> strategy = strategyMap.get( context.getMessageType());

        if (strategy == null) {
            throw new IllegalStateException("No ProcessingStrategy found for key=" +  context.getMessageType());
        }

        return (ProcessingStrategy<T>) strategy;
    }
}