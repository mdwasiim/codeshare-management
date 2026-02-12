package com.codeshare.airline.ssim.source.cmel.config;

import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelIdempotentConfig {

    @Bean("fileIdempotentRepo")
    public IdempotentRepository fileIdempotentRepo() {
        return MemoryIdempotentRepository.memoryIdempotentRepository(2000);
    }
}
