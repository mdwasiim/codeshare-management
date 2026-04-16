package com.codeshare.airline.ingestion.common.reader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LineReader {

    private final BufferedReader reader;

    public LineReader(InputStream in) {
        this.reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.US_ASCII),
                64 * 1024
        );
    }

    public String nextLine() throws IOException {

        String line;

        while ((line = reader.readLine()) != null) {

            line = line.replace("\0", "");
            line = rtrim(line);

            if (!line.isEmpty()) {
                return line;
            }
        }

        return null;
    }

    public List<String> readAll() throws IOException {

        List<String> lines = new ArrayList<>();

        String line;

        while ((line = nextLine()) != null) {
            lines.add(line);
        }

        return lines;
    }


    private String rtrim(String value) {
        int len = value.length();
        while (len > 0 && (value.charAt(len - 1) == ' ' ||
                value.charAt(len - 1) == '\0')) {
            len--;
        }
        return value.substring(0, len);
    }

}

