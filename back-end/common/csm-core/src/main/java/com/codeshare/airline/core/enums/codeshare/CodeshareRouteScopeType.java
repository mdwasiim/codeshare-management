package com.codeshare.airline.core.enums.codeshare;

public enum CodeshareRouteScopeType {

    NON_STOP_ONLY,              // Only direct sector (Origin → Destination)

    ONLINE_CONNECTION_ALLOWED,  // Can sell beyond if operated by same operating carrier

    BEYOND_ALLOWED,             // Can sell beyond agreed route

    GATEWAY_ONLY,               // Only up to/from defined gateway points

    FULL_ROUTING                // No restriction within agreement
}