package com.codeshare.airline.ssim.ingestion.reader;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SsimLineReader implements Closeable {

    private final InputStream in;

    public SsimLineReader(InputStream in) {
        this.in = new BufferedInputStream(in, 8192);
    }

    public String nextLine() throws IOException {

        byte[] buffer = in.readNBytes(200);
        if (buffer.length == 0) return null;

        if (buffer.length < 200) {
            buffer = Arrays.copyOf(buffer, 200); // pad
        }

        return new String(buffer, StandardCharsets.US_ASCII);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
