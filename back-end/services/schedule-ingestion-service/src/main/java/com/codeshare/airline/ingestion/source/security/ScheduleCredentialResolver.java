package com.codeshare.airline.ingestion.source.security;

public interface ScheduleCredentialResolver {

    String decrypt(String encryptedValue);
    String encrypt(String encryptedValue);
}