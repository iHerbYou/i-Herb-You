package com.iherbyou.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationAndValidation1234567890}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration:1800000}") // 30분
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}") // 24시간
    private Long refreshTokenExpiration;

    // JWT 서명을 위한 SecretKey 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Access Token 생성
    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpiration, "access");
    }

    // Refresh Token 생성
    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpiration, "refresh");
    }

    // 공통 토큰 생성 메서드
    private String generateToken(String email, Long expirationMs, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date((now.getTime() + expirationMs)); // 토큰 만료 날짜

        return Jwts.builder()
                .subject(email)
                .claim("type", tokenType) // 토큰 타입 구분
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    // 토큰 타입 확인
    public String getTokenType(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("type", String.class);
    }

    // 토큰에서 Claims 추출
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Access Token 유효성 검증
    public boolean validateAccessToken(String token) {
        return validateToken(token, "access");
    }

    // Refresh Token 유효성 검증
    public boolean validateRefreshToken(String token) {
        return validateToken(token, "refresh");
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = getClaimsFromToken(token);
            String tokenType = claims.get("type", String.class);

            // 토큰 타입 확인
            if (!expectedType.equals(tokenType)) {
                log.error("Token type mismatch. Expected: {}, Actual: {}", expectedType, tokenType);
                return false;
            }

            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Access Token 만료 시간 반환 (초 단위)
    public Long getAccessTokenExpirationInSeconds() {
        return accessTokenExpiration / 1000;
    }

    // Refresh Token 만료 시간 반환 (초 단위)
    public Long getRefreshTokenExpirationInSeconds() {
        return refreshTokenExpiration / 1000;
    }

}