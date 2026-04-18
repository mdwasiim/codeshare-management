package com.codeshare.airline.inbound.domain.context;

import com.codeshare.airline.enums.MessageType;
import lombok.Data;

import java.util.List;

@Data
public class ErrorContext {
    private final MessageType type;
    private final Object metadata;
    private final List<String> lines;
}