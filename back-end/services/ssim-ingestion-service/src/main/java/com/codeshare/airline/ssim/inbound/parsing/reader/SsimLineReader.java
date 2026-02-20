package com.codeshare.airline.ssim.inbound.parsing.reader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SsimLineReader implements Closeable {

    private final BufferedReader reader;

    public SsimLineReader(InputStream in) {
        this.reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.US_ASCII),
                64 * 1024   // 64KB buffer for large files
        );
    }

    public String nextLine() throws IOException {

        String line = reader.readLine();
        if (line == null) {
            return null;
        }

        // Remove right-side padding only
        return rtrim(line);
    }

    private String rtrim(String value) {
        int len = value.length();
        while (len > 0 && (value.charAt(len - 1) == ' ' ||
                value.charAt(len - 1) == '\0')) {
            len--;
        }
        return value.substring(0, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}

