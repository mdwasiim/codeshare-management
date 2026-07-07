package com.codeshare.airline.schedule.ingestion.shared.classifier;

import com.codeshare.airline.schedule.ingestion.shared.util.ActionLineParser;
import com.codeshare.airline.schedule.ingestion.shared.util.LineClassifierUtil;
import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsmMessageType;

import static com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier.*;

public class SsmLineClassifierStrategy implements LineClassifierStrategy {

    @Override
    public GenericLineClassifierContext classify(String line, ScheduleLineIdentifier lastType) {

        String normalized = line.trim().toUpperCase();

        /* ================= 1. HARD MATCHES ================= */

        if ("SSM".equals(normalized)) return new GenericLineClassifierContext(HEADER, line, normalized);
        if ("//".equals(normalized)) return new GenericLineClassifierContext(SUB_MESSAGE_SEPARATOR, line, normalized);
        if (LineClassifierUtil.isTimeMode(normalized)) return new GenericLineClassifierContext(TIME_MODE, line, normalized);

        /* ================= 2. HEADER DETAILS ================= */

        if (LineClassifierUtil.isMessageReference(normalized)) {
            return new GenericLineClassifierContext(MESSAGE_REFERENCE, line, normalized);
        }

        /* ================= 3. ACTION ================= */

        String token = ActionLineParser.parseSsm(normalized).primaryAction();
        SsmMessageType type = SsmMessageType.from(token);

        if (type != null && type != SsmMessageType.UNKNOWN) {
            return new GenericLineClassifierContext(ACTION, line, normalized, type.toActionType());
        }

        /* ================= 4. STRONG STRUCTURE ================= */

        if (LineClassifierUtil.isPeriod(normalized)) {
            return new GenericLineClassifierContext(PERIOD, line, normalized);
        }

        /* ================= HEADER DATE ================= */

        if (LineClassifierUtil.isDate(normalized)) {
            return new GenericLineClassifierContext(DATE, line, normalized);
        }

        if (LineClassifierUtil.isHeaderTime(normalized)) {
            return new GenericLineClassifierContext(HEADER_TIME, line, normalized);
        }

        if (LineClassifierUtil.isLegWithTime(normalized)) {
            return new GenericLineClassifierContext(LEG, line, normalized);
        }

        /* ================= NORMAL STRUCTURE ================= */

        if (LineClassifierUtil.isFlight(normalized)) {
            return new GenericLineClassifierContext(FLIGHT, line, normalized);
        }

        if (LineClassifierUtil.isLeg(normalized)) {
            return new GenericLineClassifierContext(LEG, line, normalized);
        }

        /* ================= CONTEXT ================= */

        if (LineClassifierUtil.isTime(normalized) && lastType == LEG) {
            return new GenericLineClassifierContext(TIME, line, normalized);
        }

        /* ================= REMAINING ================= */

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
}
