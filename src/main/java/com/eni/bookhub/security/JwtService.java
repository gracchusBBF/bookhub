package com.eni.bookhub.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.eni.bookhub.auth.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {

    public static final String SECRET = "8d9c50fe71b6c76633ebae6d833d121fe78d9953f70b0ff4c65afe1e841856e4";

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email);
    }

    public String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) // expiration after 1h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, c -> c.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims:: getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetailsImpl user) {

        try {
            final String mail = extractUsername(token);
            return (mail.equals(user.getUsername()) && !isTokenExpired(token));
        } catch (SecurityException e) {
            log.warn("invalid jwt signature: {}", e.getMessage(), e);
        } catch (MalformedJwtException e) {
            log.warn("invalid jwt token: {}", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            log.warn("jwt token is expired: {}", e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            log.warn("token is unsupported: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("token is empty: {}", e.getMessage(), e);
        }
        return false;
    }


}
