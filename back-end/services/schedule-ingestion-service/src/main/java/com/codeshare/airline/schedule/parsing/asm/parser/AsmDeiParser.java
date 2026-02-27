package com.codeshare.airline.schedule.parsing.asm.parser;


import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
import com.codeshare.airline.schedule.parsing.common.dto.InboundDei;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsmDeiParser {

    public List<InboundDei> parse(List<String> deiLines) {

        List<InboundDei> result = new ArrayList<>();

        if (deiLines == null || deiLines.isEmpty()) {
            return result;
        }

        for (String line : deiLines) {

            if (line == null || line.isBlank()) {
                continue;
            }

            // Expected format:
            // DEI 010 VALUE TEXT
            // After continuation resolver, entire value is already merged

            String[] parts = line.trim().split("\\s+", 3);

            if (parts.length < 2) {
                throw new IllegalArgumentException(
                        "Invalid DEI format in ASM: " + line);
            }

            String codeStr = parts[1];

            if (!codeStr.matches("\\d{3}")) {
                throw new IllegalArgumentException(
                        "Invalid DEI code in ASM: " + codeStr);
            }

            InboundDei dei = new InboundDei();

            dei.setDeiCode(Integer.parseInt(codeStr));
            dei.setValue(parts.length > 2 ? parts[2] : "");
            dei.setScope(DeiScopeLevel.FLIGHT_LEVEL);   // ASM DEI is flight-level

            result.add(dei);
        }

        return result;
    }
}