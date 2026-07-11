package com.codeshare.airline;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableCaching
@EnableFeignClients(basePackages = "com.codeshare.airline.identity.integration")
@SpringBootApplication(scanBasePackages = "com.codeshare.airline.identity")
public class AuthIdentityApplication {
    private static final String PASSWORD = "admin";
    private final PasswordEncoder passwordEncoder;

    public AuthIdentityApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public static void main(String[] args) {
		SpringApplication.run(AuthIdentityApplication.class, args);

	}

    @PostConstruct
    public void init() {
        String encodedPassword = passwordEncoder.encode(PASSWORD);
        System.out.println("encoded password for admin "+ encodedPassword);
    }

}
