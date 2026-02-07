package com.codeshare.airline.processor.processing.parsing;


import com.codeshare.airline.processor.pipeline.model.SsimRawLine;

public interface RecordParser<T> {

    T parse(SsimRawLine ssimRawLine);
}
