package org.youssefhergal.my_app_ws.security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class SecurityContants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 jours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String LOGIN_URL = "/users/login";
    private static final String SECRET = "votreCléSecrèteTrèsLongueEtComplexePourLaProductionAuMoins256Bits";

    public static Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes();
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }
}