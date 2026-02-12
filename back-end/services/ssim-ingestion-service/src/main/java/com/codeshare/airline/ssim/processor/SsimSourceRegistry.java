package com.codeshare.airline.ssim.processor;


import com.codeshare.airline.ssim.source.SsimSource;
import com.codeshare.airline.ssim.source.SsimSourceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SsimSourceRegistry {

    private final Map<SsimSourceType, SsimSource> sources;

    public SsimSourceRegistry(List<SsimSource> sourceList) {
        this.sources = sourceList.stream()
                .collect(Collectors.toMap(
                        SsimSource::getType,
                        Function.identity()
                ));
    }

    public SsimSource get(SsimSourceType type) {

        SsimSource source = sources.get(type);

        if (source == null) {
            throw new IllegalArgumentException(
                    "No SSIM source registered for type: " + type
            );
        }

        return source;
    }
}
