package com.codeshare.airline.ingestion.config;

import com.codeshare.airline.ingestion.source.email.EmailSourceAdapter;
import com.codeshare.airline.ingestion.source.local.LocalFolderSourceAdapter;
import com.codeshare.airline.ingestion.source.sftp.SftpSourceAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ConditionalSourceConfig {

    @Bean
    @ConditionalOnProperty(
            prefix = "ssim.sources.local",
            name = "enabled",
            havingValue = "true"
    )
    public LocalFolderSourceAdapter localSourceAdapter(LocalFolderSourceAdapter adapter) {
        return adapter;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "ssim.sources.sftp",
            name = "enabled",
            havingValue = "true"
    )
    public SftpSourceAdapter sftpSourceAdapter(SftpSourceAdapter adapter) {
        return adapter;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "ssim.sources.email",
            name = "enabled",
            havingValue = "true"
    )
    public EmailSourceAdapter emailSourceAdapter(EmailSourceAdapter adapter) {
        return adapter;
    }
}
