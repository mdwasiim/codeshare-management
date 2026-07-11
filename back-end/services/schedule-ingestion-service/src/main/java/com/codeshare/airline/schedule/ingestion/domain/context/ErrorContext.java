package com.codeshare.airline.schedule.ingestion.domain.context;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import lombok.Data;

import java.util.List;

@Data
public class ErrorContext {
    private final MessageType type;
    private final Object metadata;
    private final List<String> lines;
}