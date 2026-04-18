package com.codeshare.airline.core.enums.codeshare;

public enum CodeshareMappingType {

    /**
     * Full flight number mapping
     * Marketing flight fully maps to operating flight
     */
    FULL,

    /**
     * Mapping applied at leg level
     */
    LEG,

    /**
     * Mapping applied at segment level
     */
    SEGMENT,

    /**
     * Mapping applies only when equipment matches
     */
    EQUIPMENT_BASED,

    /**
     * Mapping applies based on day rule
     */
    DAY_BASED,

    /**
     * Wet lease or metal-neutral mapping
     */
    WET_LEASE
}