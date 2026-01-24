package com.codeshare.airline.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Deterministic UUID generator from a stable string.
 * COPY THIS FILE IDENTICALLY into tenant-service and aauth-identity-service.
 *
 * Use: CSMUuidUtil.idFor("CSM"), CSMUuidUtil.idFor("CSM-HQ"), CSMUuidUtil.idFor("DATASOURCE_PRIMARY_MYSQL")
 */
public final class CSMUuidUtil {
    private CSMUuidUtil() {}

    public static UUID idFor(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        return UUID.nameUUIDFromBytes(normalized.getBytes(StandardCharsets.UTF_8));
    }

    public static UUID fixed(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
    }
}
