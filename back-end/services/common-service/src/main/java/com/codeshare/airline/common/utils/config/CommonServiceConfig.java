package com.codeshare.airline.common.utils.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.codeshare.airline.common",
        "com.codeshare.airline.auth",
})
public class CommonServiceConfig {
}
