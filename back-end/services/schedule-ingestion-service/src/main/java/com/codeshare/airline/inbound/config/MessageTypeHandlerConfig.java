package com.codeshare.airline.inbound.config;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.inbound.orchestration.parsers.MessageParser;
import com.codeshare.airline.inbound.stream.extractor.GenericMessageExtractor;
import com.codeshare.airline.inbound.stream.extractor.SsimMessageExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MessageTypeHandlerConfig {

    // 🔥 Register Generic Extractors
    @Bean
    public StreamExtractorHandler ssmExtractor() {
        return new GenericMessageExtractor(MessageType.SSM);
    }

    @Bean
    public StreamExtractorHandler asmExtractor() {
        return new GenericMessageExtractor(MessageType.ASM);
    }

    @Bean
    public StreamExtractorHandler ssimExtractor() {
        return new SsimMessageExtractor(MessageType.SSIM);
    }

    // 🔥 Auto collect all extractors
    @Bean
    public Map<MessageType, StreamExtractorHandler> extractorMap(List<StreamExtractorHandler> handlers) {

        return handlers.stream()
                .collect(Collectors.toMap(
                        StreamExtractorHandler::supportedType,
                        Function.identity(),
                        (a, b) -> a // keep first (or log warning)
                ));
    }

    @Bean
    public Map<MessageType, MessageParser<?>> parserMap(List<MessageParser<?>> parsers) {

        return parsers.stream()
                .collect(Collectors.toMap(
                        MessageParser::supportedType,
                        Function.identity()
                ));
    }
}
