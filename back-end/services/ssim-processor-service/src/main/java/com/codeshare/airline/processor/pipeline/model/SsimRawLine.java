package com.codeshare.airline.processor.parsing.model;

import lombok.Getter;

@Getter
public class SsimRawLine {
    int lineNumber;
    String content;   // full 80-char line, untouched
}
