package com.codeshare.airline.schedule.compare.domain.enums;

/**
 * Type of change detected on a flight leg.
 * Maps 1:1 to IATA SSM sub-message action codes (Chapter 7).
 */
public enum LegChangeType {

    NEW,        // Leg not in live schedule — SSM NEW
    CNL,        // Leg in live but absent in ingested data — SSM CNL (Cancel)
    TIM,        // Timing changed: STD, STA, UTC offset, terminal — SSM TIM
    EQT,        // Equipment changed: aircraft type or configuration — SSM EQT
    RIN,        // Previously cancelled leg reinstated — SSM RIN
    PER,        // Period of operation changed: dates or days-of-operation — SSM PER
    COD,        // Codeshare designator added/removed/changed — SSM COD
    FLT         // General/combined change not fitting a single SSM type
}
