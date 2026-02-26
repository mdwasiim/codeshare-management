package com.codeshare.airline.schedule.parsing.ssm.parser;

public interface SsmParser {
    SsmInboundFileDTO parse(String content);
}