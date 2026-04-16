package com.codeshare.airline.ingestion.domain.context;

import com.codeshare.airline.core.enums.MessageType;
import lombok.Data;

import java.util.List;

@Data
public class ErrorContext {
    private final MessageType type;
    private final Object metadata;
    private final List<String> lines;
}