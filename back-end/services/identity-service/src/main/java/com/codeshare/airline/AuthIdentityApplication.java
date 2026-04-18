package com.codeshare.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(
        scanBasePackages = {"com.codeshare.airline.identity","com.codeshare.airline.web"}
)
public class AuthIdentityApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthIdentityApplication.class, args);
	}

}
