package com.codeshare.airline.ssim.processor;

import com.codeshare.airline.ssim.source.SsimSource;
import com.codeshare.airline.ssim.source.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimSourceCoordinator {

    private final SsimSourceRegistry sourceRegistry;
    private final SsimProcessor processor;

    public void ingest(SsimSourceType type)  {

        SsimSource source = sourceRegistry.get(type);

        source.fetch().forEach(processor::process);
    }
}
