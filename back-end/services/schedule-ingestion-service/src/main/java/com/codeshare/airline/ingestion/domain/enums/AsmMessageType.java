package com.codeshare.airline.ingestion.domain.enums;

public enum AsmMessageType {

    NEW(ActionType.CREATE),
    CHG(ActionType.UPDATE),
    CNL(ActionType.DELETE),
    RIN(ActionType.REINSTATE),
    RPL(ActionType.REPLACE),

    TIM(ActionType.TIME_CHANGE),          // ✅ ADD (missing!)
    RRT(ActionType.ROUTING_CHANGE),       // ✅ FIXED

    EQT(ActionType.EQUIPMENT_CHANGE),     // ✅ ADD (missing!)

    ADM(ActionType.ADMINISTRATIVE),
    ACK(ActionType.ACKNOWLEDGED),
    NAC(ActionType.NOT_ACKNOWLEDGED),

    UNKNOWN(ActionType.UNKNOWN);

    private final ActionType actionType;

    AsmMessageType(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType toActionType() {
        return actionType;
    }

    public static AsmMessageType from(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return AsmMessageType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}