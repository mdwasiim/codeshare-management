package com.codeshare.airline.ssim.inbound.orchestration.stage.file.profile;

import com.codeshare.airline.ssim.inbound.parsing.reader.SsimLineReader;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class DefaultSsimProfileValidationStage implements SsimProfileValidationStage {

    private static final int MAX_LINES_TO_SCAN = 50;

    @Override
    public SsimProfile detect(InputStream is) {

        boolean operationalSeen = false;
        boolean planningSeen = false;

        try (SsimLineReader reader = new SsimLineReader(is)) {

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
                    return SsimProfile.MIXED;
                }
            }

        } catch (Exception ex) {
            log.warn("Failed to detect SSIM profile", ex);
            return SsimProfile.UNKNOWN;
        }

        if (operationalSeen) return SsimProfile.OPERATIONAL;
        if (planningSeen)    return SsimProfile.PLANNING;

        return SsimProfile.UNKNOWN;
    }
}
