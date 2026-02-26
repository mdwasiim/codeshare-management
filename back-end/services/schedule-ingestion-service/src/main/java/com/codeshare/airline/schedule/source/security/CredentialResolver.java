package com.codeshare.airline.schedule.source.security;

public interface CredentialResolver {

    String decrypt(String encryptedValue);
}