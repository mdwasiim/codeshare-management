package com.codeshare.airline.tools.ssim.master.catalog;

import java.util.Map;

public final class MasterCatalog {
    public static final Map<String, String> TIME_MODES = Map.of(
            "L", "Local Time",
            "U", "UTC"
    );

    public static final Map<String, String> SERVICE_TYPES = Map.ofEntries(
            Map.entry("J", "Normal passenger service"),
            Map.entry("C", "Passenger charter service"),
            Map.entry("F", "Cargo/mail service"),
            Map.entry("G", "Additional passenger service"),
            Map.entry("M", "Mail service"),
            Map.entry("P", "Positioning/ferry service"),
            Map.entry("T", "Technical/test service")
    );

    public static final Map<String, String> SECURE_FLIGHT = Map.of(
            "0", "Secure flight not applicable",
            "1", "Secure flight applies"
    );

    private MasterCatalog() {
    }

    public static String name(Map<String, String> map, String code, String fallbackPrefix) {
        return map.getOrDefault(code, fallbackPrefix + " " + code);
    }
}
