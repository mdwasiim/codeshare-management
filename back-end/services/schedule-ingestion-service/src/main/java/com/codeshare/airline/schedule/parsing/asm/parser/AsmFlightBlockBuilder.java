package com.codeshare.airline.schedule.parsing.asm.parser;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmFlightBlock;
import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsmFlightBlockBuilder {

    public List<AsmFlightBlock> build(List<String> lines) {

        List<AsmFlightBlock> blocks = new ArrayList<>();
        AsmFlightBlock current = null;

        for (String line : lines) {

            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            if (trimmed.equalsIgnoreCase("ASM")) {
                continue;
            }

            if (isActionLine(trimmed)) {

                if (current != null) {
                    blocks.add(current);
                }

                current = new AsmFlightBlock();
                current.setActionIdentifier(ActionIdentifier.valueOf(trimmed));
                continue;
            }

            if (current == null) continue;

            if (isFlightLine(trimmed)) {
                current.setFlightLine(trimmed);
            }
            else if (isPeriodLine(trimmed)) {
                current.setPeriodLine(trimmed);
            }
            else if (isDaysLine(trimmed)) {
                current.setDaysLine(trimmed);
            }
            else if (isRoutingLine(trimmed)) {
                current.setRoutingLine(trimmed);
            }
            else if (trimmed.startsWith("EQT")) {
                current.getEquipmentLines().add(trimmed);
            }
            else if (trimmed.startsWith("DEI")) {
                current.getDeiLines().add(trimmed);
            }
            else if (trimmed.startsWith("SI")) {
                current.getSupplementaryLines().add(trimmed);
            }
        }

        if (current != null) {
            blocks.add(current);
        }

        return blocks;
    }

    private boolean isActionLine(String line) {
        return line.matches("NEW|CNL|RPL|TIM|EQT|ADM");
    }

    private boolean isFlightLine(String line) {
        return line.matches("^[A-Z]{2}\\d+.*");
    }

    private boolean isPeriodLine(String line) {
        return line.matches("^\\d{2}[A-Z]{3}-\\d{2}[A-Z]{3}");
    }

    private boolean isDaysLine(String line) {
        return line.matches("^[1-7]{1,7}$");
    }

    private boolean isRoutingLine(String line) {
        return line.matches("^[A-Z]{6,}\\s+\\d+.*");
    }
}