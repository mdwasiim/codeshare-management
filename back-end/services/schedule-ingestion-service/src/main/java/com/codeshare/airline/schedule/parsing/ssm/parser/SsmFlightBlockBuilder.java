package com.codeshare.airline.schedule.parsing.ssm.parser;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmFlightBlock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SsmFlightBlockBuilder {

    public List<SsmFlightBlock> build(List<String> lines) {

        List<SsmFlightBlock> blocks = new ArrayList<>();
        SsmFlightBlock currentBlock = null;

        for (String line : lines) {

            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            // Skip SSM header
            if (trimmed.equalsIgnoreCase("SSM")) {
                continue;
            }

            // New block starts when ActionIdentifier appears
            if (isActionLine(trimmed)) {

                if (currentBlock != null) {
                    blocks.add(currentBlock);
                }

                currentBlock = new SsmFlightBlock();
                currentBlock.setActionIdentifier(trimmed);
                continue;
            }

            if (currentBlock == null) {
                continue; // ignore noise before first block
            }

            if (isFlightLine(trimmed)) {
                currentBlock.setFlightDesignatorLine(trimmed);
            }
            else if (isRoutingLine(trimmed)) {
                currentBlock.setRoutingLine(trimmed);
            }
            else if (trimmed.startsWith("EQT")) {
                currentBlock.getEquipmentLines().add(trimmed);
            }
            else if (trimmed.startsWith("DEI")) {
                currentBlock.getDeiLines().add(trimmed);
            }
            else if (trimmed.startsWith("SI")) {
                currentBlock.getSupplementaryLines().add(trimmed);
            }
        }

        if (currentBlock != null) {
            blocks.add(currentBlock);
        }

        return blocks;
    }

    private boolean isActionLine(String line) {
        return line.matches("NEW|CNL|RIN|TIM|EQT|RRT|ADM");
    }

    private boolean isFlightLine(String line) {
        return line.matches("^[A-Z]{2}\\d+.*");
    }

    private boolean isRoutingLine(String line) {
        return line.matches("^[A-Z]{6,}\\s+\\d+.*");
    }
}