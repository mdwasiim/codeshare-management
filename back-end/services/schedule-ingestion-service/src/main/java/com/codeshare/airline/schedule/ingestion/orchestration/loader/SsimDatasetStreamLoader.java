package com.codeshare.airline.schedule.ingestion.orchestration.loader;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.flow.BlockTaskCoordinator;
import com.codeshare.airline.schedule.ingestion.orchestration.flow.MessageBlockProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SsimDatasetStreamLoader extends GenericStreamLoader {

    public SsimDatasetStreamLoader(Map<MessageType, StreamExtractorHandler> extractorMap,
                                   MessageBlockProcessor blockProcessor,
                                   BlockTaskCoordinator taskCoordinator) {
        super(extractorMap, blockProcessor, taskCoordinator);
    }

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.SSIM;
    }
}
