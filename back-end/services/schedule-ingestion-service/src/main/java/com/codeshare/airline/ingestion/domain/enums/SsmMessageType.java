package com.codeshare.airline.ingestion.domain.enums;


public enum SsmMessageType {

    NEW(ActionType.CREATE),
    CHG(ActionType.UPDATE),
    CNL(ActionType.DELETE),
    RPL(ActionType.REPLACE),

    EQT(ActionType.EQUIPMENT_CHANGE),
    TIM(ActionType.TIME_CHANGE),

    ACK(ActionType.ACKNOWLEDGED),
    NAC(ActionType.NOT_ACKNOWLEDGED),
    ADM(ActionType.ADMINISTRATIVE),

    UNKNOWN(ActionType.UNKNOWN);

    private final ActionType actionType;

    SsmMessageType(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType toActionType() {
        return actionType;
    }

    public static SsmMessageType from(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return SsmMessageType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null; // 🔥 NOT UNKNOWN
        }
    }
}