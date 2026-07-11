package com.codeshare.airline.schedule.ingestion.extraction.extractor;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.shared.reader.LineReader;
import com.codeshare.airline.schedule.ingestion.shared.util.LineClassifierUtil;
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

        LineClassifier classifier = LineClassifierFactory.forMessage(messageType);
        classifier.reset();

        LineReader reader = new LineReader(inputStream);

        try {
            String rawLine;
            while ((rawLine = reader.nextLine()) != null) {
                String normalized = rawLine.trim();
                if (normalized.isEmpty()) {
                    continue;
                }

                if (isTypeBStart(normalized)) {
                    List<String> rawTypeB = new ArrayList<>();
                    rawTypeB.add(rawLine);

                    String next;
                    while ((next = reader.nextLine()) != null) {
                        String nextNormalized = next.trim();
                        if (nextNormalized.isEmpty()) {
                            continue;
                        }

                        rawTypeB.add(next);
                        if (LineClassifierUtil.isBlockEnd(nextNormalized)) {
                            break;
                        }
                    }

                    header.clear();
                    current = new ArrayList<>();

                    List<String> normalizedAsm = TypeBToAsmConverter.convert(rawTypeB);
                    for (String line : normalizedAsm) {
                        current = processLine(line, line, header, current, consumer, classifier);
                    }
                    continue;
                }

                current = processLine(rawLine, normalized, header, current, consumer, classifier);
            }

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }
        } catch (Exception ex) {
            log.error("{} extraction failed", messageType, ex);
            throw new IllegalStateException(messageType + " extraction failed", ex);
        }
    }

    private List<String> processLine(
            String rawLine,
            String normalized,
            List<String> header,
            List<String> current,
            Consumer<List<String>> consumer,
            LineClassifier classifier
    ) {
        if (normalized.equalsIgnoreCase(messageType.name())) {
            classifier.reset();

            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            header.clear();
            header.add(rawLine);
            return current;
        }

        if (MessageExtractorUtil.isHeaderLine(normalized, messageType)) {
            header.add(rawLine);
            return current;
        }

        GenericLineClassifierContext context = classifier.classify(rawLine);
        if (context.getActionType() != null && context.getActionType() != ActionType.UNKNOWN) {
            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            List<String> newCurrent = new ArrayList<>(header);
            newCurrent.add(rawLine);
            return newCurrent;
        }

        if (LineClassifierUtil.isBlockEnd(normalized)) {
            if (MessageExtractorUtil.isValidSubMessage(current, messageType)) {
                MessageExtractorUtil.flush(current, consumer);
            }

            header.clear();
            return new ArrayList<>();
        }

        current.add(rawLine);
        return current;
    }

    private boolean isTypeBStart(String line) {
        return line.matches("^[A-Z]{2}[A-Z0-9]{5,6}$");
    }
}
