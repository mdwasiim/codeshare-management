package com.codeshare.airline.schedule.orchestration.stage.ssim.file.profile;

import com.codeshare.airline.schedule.parsing.common.reader.LineReader;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class DefaultSsimProfileValidationStage implements SsimProfileValidationStage {

    private static final int MAX_LINES_TO_SCAN = 50;

    @Override
    public ScheduleProfile detect(InputStream is) {

        boolean operationalSeen = false;
        boolean planningSeen = false;

        try (LineReader reader = new LineReader(is)) {

            for (int i = 0; i < MAX_LINES_TO_SCAN; i++) {

                String line = reader.nextLine();
                if (line == null) break;

                if (line.isBlank()) continue;

                line = line.replaceAll("^[\\r\\n]+", "");
                char recordType = line.charAt(0);

                // Operational SSIM (T1–T5)
                if (recordType >= '1' && recordType <= '5') {
                    operationalSeen = true;
                }

                // Planning SSIM (R1–R6)
                if (recordType == 'R' || recordType == '6') {
                    planningSeen = true;
                }

                if (operationalSeen && planningSeen) {
                    return ScheduleProfile.MIXED;
                }
            }

        } catch (Exception ex) {
            log.warn("Failed to detect SSIM profile", ex);
            return ScheduleProfile.UNKNOWN;
        }

        if (operationalSeen) return ScheduleProfile.OPERATIONAL;
        if (planningSeen)    return ScheduleProfile.PLANNING;

        return ScheduleProfile.UNKNOWN;
    }
}
