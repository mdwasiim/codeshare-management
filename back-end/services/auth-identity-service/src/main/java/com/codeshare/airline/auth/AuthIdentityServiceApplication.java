package com.codeshare.airline.auth;

import com.codeshare.airline.common.utils.config.CommonServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CommonServiceConfig.class)
public class AuthIdentityServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthIdentityServiceApplication.class, args);
	}

}
