package com.codeshare.airline.platform.web.autoconfigure;

import com.codeshare.airline.platform.web.exception.GlobalExceptionHandler;
import com.codeshare.airline.platform.web.filter.CSMRequestContextFilter;
import com.codeshare.airline.platform.web.response.CSMResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CSMWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CSMRequestContextFilter csmRequestContextFilter() {
        return new CSMRequestContextFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CSMResponseHandler csmResponseHandler(ObjectMapper objectMapper) {
        return new CSMResponseHandler(objectMapper);
    }
}
