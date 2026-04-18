package com.codeshare.airline.core.enums.auth;

public enum AuthSource {

    INTERNAL,   // Username/password in your system
    LDAP,       // Corporate directory
    AZURE ;     // Azure AD (OIDC)

    public boolean isOidc() {
        return this == AZURE ;
    }
}
