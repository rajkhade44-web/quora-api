package quora_api.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public ResponseCookie creatResponseTokenCookie(String refreshToken) {
        return ResponseCookie.from(refreshToken)
                .domain(cookieDomain)
                .secure(cookieSecure)
                .httpOnly(true)
                .path("/auth")
                .maxAge(refreshExpirationMs / 1000)
                .sameSite("Strict")
                .build();
    }
    
    public ResponseCookie removeResponseTokenCookie() {
        return ResponseCookie.from("referesh_token","")
            .httpOnly(true)
            .domain(cookieDomain)
            .secure(cookieSecure)
            .maxAge(0)
            .path("/auth")
            .sameSite("Strict")
            .build();
    }
}
