package com.codeshare.airline.inbound.orchestration.loader;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.handler.ScheduleTypeHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTypeHandlerResolver {

    private final List<ScheduleTypeHandler<?, ?>> handlers;

    private final Map<MessageType, ScheduleTypeHandler<?, ?>> handlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (ScheduleTypeHandler<?, ?> handler : handlers) {

            MessageType type = handler.supportedType();

            if (handlerMap.containsKey(type)) {
                throw new IllegalStateException("Duplicate handler for type=" + type);
            }

            handlerMap.put(type, handler);

            //  ADD LOG HERE
            log.info("Registered handler | type={} | handler={}",
                    type, handler.getClass().getSimpleName());
        }

        //  OPTIONAL: summary log
        log.info("Total handlers registered: {}", handlerMap.size());
    }

    @SuppressWarnings("unchecked")
    public <TParsed, TMetadata> ScheduleTypeHandler<TParsed, TMetadata> resolve(MessageType type) {

        ScheduleTypeHandler<?, ?> handler = handlerMap.get(type);

        if (handler == null) {
            throw new IllegalStateException("No handler for " + type);
        }

        return (ScheduleTypeHandler<TParsed, TMetadata>) handler;
    }
}