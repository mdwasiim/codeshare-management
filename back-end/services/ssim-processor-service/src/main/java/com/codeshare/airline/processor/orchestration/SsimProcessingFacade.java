package com.codeshare.airline.processor.orchestration;

import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import com.codeshare.airline.processor.pipeline.model.SsimRawFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class SsimProcessingFacade {

    private final SsimRawLoadCoordinator rawLoadCoordinator;
    private final SsimBusinessProcessingCoordinator businessCoordinator;

    public void process(SsimRawFile ssimRawFile) {

        SsimLoadContext context = rawLoadCoordinator.load(ssimRawFile);

        businessCoordinator.process(context);

    }
}

