package com.codeshare.airline.schedule.parsing.ssm.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SsmContinuationResolver {

    public List<String> resolve(List<String> rawLines) {

        List<String> resolved = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String line : rawLines) {

            if (line.startsWith(" ")) {
                current.append(" ").append(line.trim());
            } else {
                if (!current.isEmpty()) {
                    resolved.add(current.toString());
                }
                current = new StringBuilder(line.trim());
            }
        }

        if (!current.isEmpty()) {
            resolved.add(current.toString());
        }

        return resolved;
    }
}