package com.codeshare.airline.auth.service;

import com.codeshare.airline.core.enums.AuthSource;

public interface AuthIdentityProviderService {
     void assertProviderEnabled(String tenantId, AuthSource source) ;
}
