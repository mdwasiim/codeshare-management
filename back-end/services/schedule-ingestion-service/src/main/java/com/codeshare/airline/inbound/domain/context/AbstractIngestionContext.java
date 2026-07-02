package com.codeshare.airline.inbound.domain.context;

import com.codeshare.airline.core.enums.schedule.MessageType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractIngestionContext<TMeta, TParsed> {

    private final MessageType messageType;

    protected final TMeta metadata;

    protected final TParsed parsedData;
}