package com.codeshare.airline.schedule.parsing.ssm.parser;

import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
import com.codeshare.airline.schedule.parsing.common.dto.InboundDei;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SsmDeiParser {

    public List<InboundDei> parse(List<String> deiLines) {

        List<InboundDei> result = new ArrayList<>();

        for (String line : deiLines) {

            String[] parts = line.split("\\s+", 3);

            InboundDei dei = new InboundDei();
            dei.setDeiCode(Integer.parseInt(parts[1]));
            dei.setValue(parts.length > 2 ? parts[2] : "");
            dei.setScope(DeiScopeLevel.FLIGHT_LEVEL);

            result.add(dei);
        }

        return result;
    }
}