package com.codeshare.airline.ingestion.parsing.line;

import com.codeshare.airline.ingestion.model.SsimRawFile;

import java.util.function.Consumer;

public interface SsimLineReader {
    void readLines(SsimRawFile file, Consumer<RawSsimLine> consumer);
}
