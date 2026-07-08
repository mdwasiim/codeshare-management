package com.codeshare.airline.schedule.ingestion.config;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.extraction.extractor.GenericMessageExtractor;
import com.codeshare.airline.schedule.ingestion.extraction.extractor.SsimMessageExtractor;
import com.codeshare.airline.schedule.ingestion.orchestration.context.PreParseContextFactory;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MessageTypeHandlerConfig {

    @Bean
    public StreamExtractorHandler ssmExtractor() {
        return new GenericMessageExtractor(MessageType.SSM);
    }

    @Bean
    public StreamExtractorHandler asmExtractor() {
        return new GenericMessageExtractor(MessageType.ASM);
    }

    @Bean
    public StreamExtractorHandler ssimExtractor(ScheduleIngestionProperties properties) {
        return new SsimMessageExtractor(MessageType.SSIM, properties.getSsim().getFlightBatchSize());
    }

    @Bean
    public Map<MessageType, StreamExtractorHandler> extractorMap(List<StreamExtractorHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        StreamExtractorHandler::supportedType,
                        Function.identity(),
                        (existing, ignored) -> existing
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

    @Bean
    public Map<MessageType, PreParseContextFactory<?>> preParseContextFactoryMap(List<PreParseContextFactory<?>> factories) {
        return factories.stream()
                .collect(Collectors.toMap(
                        PreParseContextFactory::supportedType,
                        Function.identity()
                ));
    }
}
