package com.codeshare.airline.schedule.ingestion.domain.enums;

/*
public enum ActionType {

    CREATE,            // NEW
    UPDATE,            // CHG / FLT
    DELETE,            // CNL
    REPLACE,           // RPL

    TIME_CHANGE,       // TIM / RRT / RSD
    SCHEDULE_CHANGE,   // SKD

    EQUIPMENT_CHANGE,  // EQT
    CONNECTION_UPDATE, // CON

    REINSTATE,         // RIN
    REVISION,          // REV

    ACKNOWLEDGED,      // ACK
    NOT_ACKNOWLEDGED,  // NAC
    ADMINISTRATIVE,    // ADM

    BASELINE_LOAD,     // SSIM

    UNKNOWN
}*/

public enum ActionType {

    CREATE,              // NEW
    UPDATE,              // CHG
    DELETE,              // CNL
    REPLACE,             // RPL
    SCHEDULE_CHANGE,     // SKD
    REVISION,            // REV
    REQUEST_SCHEDULE_DATA, // RSD
    IDENTIFIER_CHANGE,   // FLT

    TIME_CHANGE,         // TIM
    ROUTING_CHANGE,      // RRT
    EQUIPMENT_CHANGE,    // EQT
    CONFIGURATION_CHANGE, // CON

    REINSTATE,           // RIN

    ACKNOWLEDGED,        // ACK
    NOT_ACKNOWLEDGED,    // NAC
    ADMINISTRATIVE,      // ADM

    UNKNOWN,

    BASELINE_LOAD // used for SSIM
}
