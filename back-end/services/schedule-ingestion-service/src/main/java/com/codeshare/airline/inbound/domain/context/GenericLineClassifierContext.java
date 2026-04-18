package com.codeshare.airline.inbound.domain.context;

import com.codeshare.airline.inbound.domain.enums.ActionType;
import com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier;
import lombok.Getter;

@Getter
public final class GenericLineClassifierContext {

    private final ScheduleLineIdentifier type;
    private final String rawLine;
    private final String normalizedLine;
    private final ActionType actionType;

    public GenericLineClassifierContext(ScheduleLineIdentifier type, String rawLine, String normalizedLine) {
        this(type, rawLine, normalizedLine, ActionType.UNKNOWN);
    }

    public GenericLineClassifierContext(ScheduleLineIdentifier type, String rawLine, String normalizedLine, ActionType actionType) {
        this.type = type;
        this.rawLine = rawLine;
        this.normalizedLine = normalizedLine;
        this.actionType = actionType != null ? actionType : ActionType.UNKNOWN;
    }

    @Override
    public String toString() {
        return "GenericLineClassifierContext{" +
                "type=" + type +
                ", actionType=" + actionType +
                ", rawLine='" + rawLine + '\'' +
                '}';
    }
}