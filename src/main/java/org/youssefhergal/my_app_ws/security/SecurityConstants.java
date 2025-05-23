package org.youssefhergal.my_app_ws.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String LOGIN_URL = "/users/login";

    // Use a simple string secret instead of dynamic key generation
    private static final String SECRET = "ThisIsASecretKeyForJWTGenerationThisIsASecretKeyForJWTGeneration";
    public static final byte[] TOKEN_SECRET = SECRET.getBytes(StandardCharsets.UTF_8);
}