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

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultScheduleIngestionProcessor implements ScheduleIngestionProcessor {

    private final List<ScheduleChapterProcessor> chapterProcessors;

    @Override
    public void process(ScheduleSourceFile sourceFile) {

        ScheduleMessageType type =
                sourceFile.withStream(this::detect);

        ScheduleChapterProcessor processor = chapterProcessors.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No processor for type " + type));

        processor.process(sourceFile);
    }


    public ScheduleMessageType detect(InputStream is) {

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(is))) {

            String firstLine = reader.readLine();

            if (firstLine.startsWith("SSM")) return ScheduleMessageType.SSM;
            if (firstLine.startsWith("ASM")) return ScheduleMessageType.ASM;

            return ScheduleMessageType.SSIM;

        } catch (IOException e) {
            throw new IllegalStateException("Detection failed", e);
        }
    }
}