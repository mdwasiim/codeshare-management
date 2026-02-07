package com.codeshare.airline.ingestion.util;

import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.parsing.line.RawSsimLine;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class SsimFileUtils {

    private SsimFileUtils() {}

    public static List<RawSsimLine> toLines(SsimRawFile rawFile) {

        String content = new String(rawFile.getContent(), StandardCharsets.UTF_8);
        AtomicInteger lineNo = new AtomicInteger(1);

        return content.lines()
                .filter(l -> !l.isBlank())
                .map(l -> RawSsimLine.builder()
                        .lineNo(lineNo.getAndIncrement())
                        .content(padRight(l, 80))
                        .build()
                )
                .toList();
    }


    private static String padRight(String line, int length) {
        return line.length() >= length
                ? line.substring(0, length)
                : String.format("%-" + length + "s", line);
    }

    public static String sha256(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
