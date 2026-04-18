package com.codeshare.airline.inbound.source.security;

public interface ScheduleCredentialResolver {

    String decrypt(String encryptedValue);
    String encrypt(String encryptedValue);
}