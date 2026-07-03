package com.codeshare.airline.schedule.ingestion.domain.enums;

public enum ScheduleLineIdentifier {

    // 1. Message Heading
    HEADER,                          // ASM / SSM

    // 2. Header Details (SSM)
    DATE,                            // 12MAR
    HEADER_TIME,                     // 1234

    // 3. Time mode
    TIME_MODE,                       // UTC / LT

    // 4. Message Reference
    MESSAGE_REFERENCE,               // REF / header reference

    // 5. Action (MANDATORY)
    ACTION,                          // NEW / CHG / CNL / TIM / RRT / EQT

    // 6. Flight Identification
    FLIGHT,                          // QR123 DOHLHR 15APR

    // 7. Period (SSM only)
    PERIOD,                          // 01APR30SEP 1234567

    // 8. Leg Information
    LEG,                             // DOH LHR ...

    // 9. Equipment / Service
    EQUIPMENT_AND_SERVICE,           // 320 Y

    // 10. Data Elements (CRITICAL)
    DEI,                             // /XXX

    // 11. Supplementary Info
    SUPPLEMENTARY,                   // SI ...

    // 12. Sub-message Separator
    SUB_MESSAGE_SEPARATOR,           // //

    // 13. Time Information (ASM TIM / SSM leg time)
    TIME,                            // 0900 1400

    // fallback
    UNKNOWN
}