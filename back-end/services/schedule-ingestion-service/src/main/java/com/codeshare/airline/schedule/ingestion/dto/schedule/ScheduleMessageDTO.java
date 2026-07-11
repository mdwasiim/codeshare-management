package com.codeshare.airline.schedule.ingestion.dto.schedule;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMessageDTO extends CSMAuditableDTO {

    /* ================= FILE / CONTEXT ================= */

    private MessageType messageType;   // ✅ MUST persist (SSM / ASM)

    private String source;

    /* ================= HEADER ================= */

    private String sender;
    private String recipient;

    private TimeMode timeMode;           // 🔥 LT / UTC (MANDATORY)

    private LocalDate creationDate;    // 🔥 parsed date
    private String creationDateRaw;    // keep raw (SSIM format)

    private String creationTime;

    private String creatorReference;
    private String messageReference;

    /* ================= RAW ================= */

    private String rawHeader;

    @Builder.Default
    private List<String> rawLines = new ArrayList<>();

    public void addRawLine(String line) {
        if (line != null) rawLines.add(line);
    }

    /* ================= PROCESSING ================= */

    private String processingStatus;   // SUCCESS / FAILED / PARTIAL
    private String errorMessage;

    /* ================= CHILDREN ================= */

    @Builder.Default
    private List<ScheduleSubMessageDTO> messages = new ArrayList<>();

    public void addMessage(ScheduleSubMessageDTO message) {
        if (message != null) {
            message.setMessageSequenceNumber(messages.size() + 1);
            messages.add(message);
        }
    }
}