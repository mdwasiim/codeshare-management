package com.codeshare.airline.schedule.parsing.common.dto;

public enum ActionIdentifier {

    NEW,
    CNL,
    RIN,   // SSM only
    RPL,   // ASM only
    TIM,
    EQT,
    RRT,   // SSM only
    ADM;

    public static ActionIdentifier from(String value) {
        return ActionIdentifier.valueOf(value.toUpperCase());
    }

    public boolean allowedForSsm() {
        return switch (this) {
            case NEW, CNL, RIN, TIM, EQT, RRT, ADM -> true;
            case RPL -> false;
        };
    }

    public boolean allowedForAsm() {
        return switch (this) {
            case NEW, CNL, RPL, TIM, EQT, ADM -> true;
            case RIN, RRT -> false;
        };
    }
}