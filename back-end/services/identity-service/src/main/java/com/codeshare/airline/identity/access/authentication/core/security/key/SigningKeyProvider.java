package com.codeshare.airline.identity.access.authentication.core.security.key;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;

import java.security.KeyPair;

public interface SigningKeyProvider {

    KeyPair getKeyPair() throws JOSEException;              // for low-level crypto if needed

    JWKSet getPrivateJwkSet();          // 🔐 signing ONLY

    JWKSet getPublicJwkSet();           // 🔓 verification ONLY
}
