package com.codeshare.airline.schedule.ingestion.extraction.extractor;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.shared.reader.LineReader;
import com.codeshare.airline.schedule.ingestion.shared.util.LineClassifierUtil;
import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class GenericMessageExtractor implements StreamExtractorHandler {

    private final MessageType messageType;

    public GenericMessageExtractor(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public MessageType supportedType() {
        return messageType;
    }

    @Override
    public void extract(InputStream inputStream, Consumer<List<String>> consumer) {

        List<String> header = new ArrayList<>();
        List<String> current = new ArrayList<>();

        LineClassifier classifier = LineClassifierFactory.create(messageType);
        assert classifier != null;
        classifier.reset();

        LineReader reader = new LineReader(inputStream);

        try {
            String rawLine;

            while ((rawLine = reader.nextLine()) != null) {

                String normalized = rawLine.trim();
                if (normalized.isEmpty()) continue;

                /* ================= TYPE B DETECTION ================= */

                if (isTypeBStart(normalized)) {

                    List<String> rawTypeB = new ArrayList<>();
                    rawTypeB.add(rawLine);

                    String next;

                    while ((next = reader.nextLine()) != null) {

                        String n = next.trim();
                        if (n.isEmpty()) continue;

                        rawTypeB.add(next);

                        if (LineClassifierUtil.isBlockEnd(n)) {
                            break;
                        }
                    }

                    // 🔥 RESET STATE
                    header.clear();
                    current = new ArrayList<>();

                    List<String> normalizedAsm =
                            TypeBToAsmConverter.convert(rawTypeB);

                    // 🔥 PROCESS EACH LINE
                    for (String line : normalizedAsm) {
                        current = processLine(line, line, header, current, consumer, classifier);
                    }

                    continue;
                }

                /* ================= NORMAL FLOW ================= */

                current = processLine(rawLine, normalized, header, current, consumer, classifier);
            }

            /* ================= FINAL FLUSH ================= */

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

        } catch (Exception ex) {
            log.error("{} extraction failed", messageType, ex);
            throw new IllegalStateException(messageType + " extraction failed", ex);
        }
    }

    /* =========================================================
       CORE LINE PROCESSOR (REUSABLE)
       ========================================================= */

    private List<String> processLine(
            String rawLine,
            String normalized,
            List<String> header,
            List<String> current,
            Consumer<List<String>> consumer,
            LineClassifier classifier) {

        /* ================= MESSAGE START ================= */

        if (normalized.equalsIgnoreCase(messageType.name())) {

            classifier.reset(); // 🔥 important

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            header.clear();
            header.add(rawLine);

            return current;
        }

        /* ================= HEADER ================= */

        if (MessageExtractorUtil.isHeaderLine(normalized, messageType)) {
            header.add(rawLine);
            return current;
        }

        /* ================= ACTION ================= */

        GenericLineClassifierContext ctx = classifier.classify(rawLine);

        if (ctx.getActionType() != null &&
                ctx.getActionType() != ActionType.UNKNOWN) {

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            List<String> newCurrent = new ArrayList<>(header);
            newCurrent.add(rawLine);

            return newCurrent;
        }

        /* ================= BLOCK END ================= */

        if (LineClassifierUtil.isBlockEnd(normalized)) {

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            header.clear(); // 🔥 important
            return new ArrayList<>();
        }

        /* ================= BODY ================= */

        current.add(rawLine);
        return current;
    }

    /* =========================================================
       TYPE B DETECTION
       ========================================================= */

    private boolean isTypeBStart(String line) {
        return line.matches("^[A-Z]{2}[A-Z0-9]{5,6}$"); // e.g. QKDOHQR
    }
}
