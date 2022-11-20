package pl.com.seremak.assetsmanagement.utils;

import org.springframework.security.oauth2.jwt.Jwt;

public class HttpClientUtils {

    public static final String BEARER_TOKEN_PATTERN = "Bearer %s";
    public static final String URI_SEPARATOR = "/%s";

    public static String prepareBearerToken(final Jwt token) {
        return BEARER_TOKEN_PATTERN.formatted(token.getTokenValue());
    }
}
