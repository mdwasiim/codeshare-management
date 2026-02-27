package com.codeshare.airline.schedule.orchestration;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.codeshare.airline.schedule.domain.common.ScheduleMessageType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultScheduleIngestionProcessor implements ScheduleIngestionProcessor {

    private final List<ScheduleChapterProcessor> chapterProcessors;

    @Override
    public void process(ScheduleSourceFile sourceFile) {

        ScheduleMessageType headerType =
                sourceFile.getScheduleMessageType();

        ScheduleMessageType detectedType =
                sourceFile.withStream(this::detect);

        if (!headerType.equals(detectedType)) {
            throw new IllegalStateException(
                    "Header MESSAGE_TYPE mismatch. Header=" +
                            headerType + ", Detected=" + detectedType);
        }

        ScheduleChapterProcessor processor = chapterProcessors.stream()
                .filter(p -> p.supports(detectedType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No processor for type " + detectedType));

        processor.process(sourceFile);
    }


    public ScheduleMessageType detect(InputStream is) {

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    break;
                }
            }

            if (line == null) throw new IllegalStateException("Empty file");

            line = line.toUpperCase();

            if (line.startsWith("SSM")) return SSM;
            if (line.startsWith("ASM")) return ASM;
            if (line.startsWith("1") || line.length() == 200) return SSIM;

        } catch (IOException e) {
            throw new IllegalStateException("Detection failed", e);
        }
        return null;
    }
}