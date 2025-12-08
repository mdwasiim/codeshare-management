package com.codeshare.airline.common.services.utils.helper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Deterministic UUID generator from a stable string.
 * COPY THIS FILE IDENTICALLY into tenant-service and auth-identity-service.
 *
 * Use: UuidUtil.idFor("CSM"), UuidUtil.idFor("CSM-HQ"), UuidUtil.idFor("DATASOURCE_PRIMARY_MYSQL")
 */
public final class UuidUtil {
    private UuidUtil() {}

    public static UUID idFor(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        return UUID.nameUUIDFromBytes(normalized.getBytes(StandardCharsets.UTF_8));
    }

    public static UUID fixed(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
    }
}
