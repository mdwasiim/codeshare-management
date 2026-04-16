package com.codeshare.airline.ingestion.common.classifier;

import com.codeshare.airline.ingestion.common.util.LineClassifierUtil;
import com.codeshare.airline.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.ingestion.domain.enums.ScheduleLineIdentifier;

import static com.codeshare.airline.ingestion.domain.enums.ScheduleLineIdentifier.*;

public class AsmLineClassifierStrategy implements LineClassifierStrategy {

    @Override
    public GenericLineClassifierContext classify(String line, ScheduleLineIdentifier lastType) {

        String normalized = line.trim().toUpperCase();

        /* ================= HARD MATCHES ================= */

        if ("//".equals(normalized)) {
            return new GenericLineClassifierContext(SUB_MESSAGE_SEPARATOR, line, normalized);
        }

        if ("ASM".equals(normalized)) {
            return new GenericLineClassifierContext(HEADER, line, normalized);
        }

        if (LineClassifierUtil.isTimeMode(normalized)) {
            return new GenericLineClassifierContext(TIME_MODE, line, normalized);
        }

        /* ================= HEADER DETAILS ================= */

        if (LineClassifierUtil.isDate(normalized)) {
            return new GenericLineClassifierContext(DATE, line, normalized);
        }

        if (LineClassifierUtil.isHeaderTime(normalized)) {
            return new GenericLineClassifierContext(HEADER_TIME, line, normalized);
        }

        if (LineClassifierUtil.isMessageReference(normalized)) {
            return new GenericLineClassifierContext(MESSAGE_REFERENCE, line, normalized);
        }

        /* ================= ACTION ================= */

        String token = extractFirstToken(normalized);

        AsmMessageType type = AsmMessageType.from(token);

        if (type != null && type != AsmMessageType.UNKNOWN) {
            return new GenericLineClassifierContext(
                    ACTION,
                    line,
                    normalized,
                    type.toActionType()
            );
        }

        /* ================= CORE STRUCTURE ================= */
        if (LineClassifierUtil.isFlightWithRoute(normalized)) {
            return new GenericLineClassifierContext(FLIGHT, line, normalized);
        }

        if (LineClassifierUtil.isLegWithTime(normalized)) {
            return new GenericLineClassifierContext(LEG, line, normalized);
        }
        if (LineClassifierUtil.isFlight(normalized)) {
            return new GenericLineClassifierContext(FLIGHT, line, normalized);
        }

        if (LineClassifierUtil.isLeg(normalized)) {
            return new GenericLineClassifierContext(LEG, line, normalized);
        }

        if (LineClassifierUtil.isPeriod(normalized)) {
            return new GenericLineClassifierContext(PERIOD, line, normalized);
        }

        if (LineClassifierUtil.isTime(normalized) && lastType == LEG) {
            return new GenericLineClassifierContext(TIME, line, normalized);
        }

        if (LineClassifierUtil.isEquipment(normalized)) {
            return new GenericLineClassifierContext(EQUIPMENT_AND_SERVICE, line, normalized);
        }

        if (LineClassifierUtil.isDei(normalized)) {
            return new GenericLineClassifierContext(DEI, line, normalized);
        }

        if (normalized.startsWith("SI ")) {
            return new GenericLineClassifierContext(SUPPLEMENTARY, line, normalized);
        }

        return new GenericLineClassifierContext(UNKNOWN, line, normalized);
    }
    private String extractFirstToken(String line) {
        int idx = line.indexOf(' ');
        return idx > 0 ? line.substring(0, idx) : line;
    }
}