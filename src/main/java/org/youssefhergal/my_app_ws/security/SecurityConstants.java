package org.youssefhergal.my_app_ws.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jdk.internal.jimage.decompressor.StringSharingDecompressor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 jours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String LOGIN_URL = "/users/login";
    private static SecretKey Key   = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final String TOKEN_SECRET = Base64.getEncoder().encodeToString(getKey().getEncoded());



    public static SecretKey getKey() {
        return Key;
    }

    public static void setKey(SecretKey key) {
        Key = key;
    }
}