package com.codeshare.airline.ingestion.parsing;

import com.codeshare.airline.ingestion.event.SsimEventPublisher;
import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.parsing.line.RawSsimLine;
import com.codeshare.airline.ingestion.parsing.record.SsimRecord;
import com.codeshare.airline.ingestion.parsing.router.SsimRecordRouter;
import com.codeshare.airline.ingestion.util.ProcessedFileRegistry;
import com.codeshare.airline.ingestion.util.SsimFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SsimParsingPipeline {

    private final SsimRecordRouter router;
    private final SsimEventPublisher publisher;
    private final ProcessedFileRegistry registry;

    public void process(SsimRawFile rawFile) {

        String hash = SsimFileUtils.sha256(rawFile.getContent());

        if (registry.alreadyProcessed(hash)) {
            return;
        }

        List<RawSsimLine> lines = SsimFileUtils.toLines(rawFile);

        List<SsimRecord> records = lines.stream()
                .map(router::route)
                .toList();

        publisher.publish(rawFile, records);
        registry.markProcessed(hash);
    }
}



