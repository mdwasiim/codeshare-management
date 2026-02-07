package com.codeshare.airline.processor.parsing.model;

import lombok.Getter;

import java.util.List;

@Getter
public class SsimRawFile {
    String fileName;
    List<SsimRawLine> lines;
}
