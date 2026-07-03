package com.codeshare.airline.identity.access.authentication.service;

import com.codeshare.airline.core.enums.auth.AuthSource;

public interface AuthIdentityProviderService {
     void assertProviderEnabled(String tenantId, AuthSource source) ;
}
