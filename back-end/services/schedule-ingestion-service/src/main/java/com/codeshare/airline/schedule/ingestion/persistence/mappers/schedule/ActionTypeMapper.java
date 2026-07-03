package com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsmMessageType;

public final class ActionTypeMapper {

    public static ActionType fromAsm(String value) {

        AsmMessageType type = AsmMessageType.from(value);

        if (type == null || type == AsmMessageType.UNKNOWN) {
            return ActionType.UNKNOWN;
        }

        return type.toActionType();
    }

    public static ActionType fromSsm(String value) {

        SsmMessageType type = SsmMessageType.from(value);

        if (type == null || type == SsmMessageType.UNKNOWN) {
            return ActionType.UNKNOWN;
        }

        return type.toActionType();
    }
}