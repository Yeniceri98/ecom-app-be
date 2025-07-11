package org.application.ecomappbe.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.security.user.EcomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationTimeMs}")
    private int expirationTimeMs;

    @Value("${jwt.cookie}")
    private String jwtCookie;

    // Token Based Authentication - Get JWT from Header
    public String getJwtFromHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);     // EX: Bearer 12301231231231230913
        }

        return null;
    }

    // Token Based Authentication - Generate JWT Token
    public String generateJwtToken(Authentication authentication) {
        EcomUserDetails userDetails = (EcomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(signingKey())
                .compact();
    }

    // Cookie Based Authentication - Get JWT from Cookies
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);

        if (cookie != null) {
            return cookie.getValue();
        }

        return null;
    }

    // Cookie Based Authentication - Generate JWT Cookie
    public ResponseCookie generateJwtCookie(EcomUserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());

        return ResponseCookie.from(jwtCookie, jwt)
                .path("/api")       // Cookie will send the URLs that only start with "/api"
                .maxAge(24 * 60 * 60)
                .httpOnly(false)    // XSS Protection (If set true, JavaScript cannot access the cookie)
                .sameSite("Lax")    // CSRF Protection
                .secure(false)      // NOTE: It has to be "true" in production
                .build();
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(signingKey())
                .compact();
    }

    // For Logout
    // NOTE: Parameters like "path", "sameSite", "httpOnly" and "secure" should be set in the same way as in the generateJwtCookie() method
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from("jwt", "")
                .path("/api")
                .maxAge(0)
                .httpOnly(true)
                .sameSite("Lax")
                .secure(false)
                .build();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
