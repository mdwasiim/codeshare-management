package com.codeshare.airline.schedule.ingestion.source.security;

public interface ScheduleCredentialResolver {

    String decrypt(String encryptedValue);
    String encrypt(String encryptedValue);
}