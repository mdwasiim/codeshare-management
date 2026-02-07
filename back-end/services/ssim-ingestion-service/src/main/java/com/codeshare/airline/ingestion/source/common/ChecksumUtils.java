package com.codeshare.airline.ingestion.source.common;

import java.security.MessageDigest;
import java.util.HexFormat;

public final class ChecksumUtils {

    private ChecksumUtils() {}

    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
