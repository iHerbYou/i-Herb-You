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

    @Value("${jwt.expiration:86400000}") // 24시간 (밀리초)
    private Long jwtExpirationMs;

    // JWT 서명을 위한 SecretKey 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // JWT 토큰 생성 (email을 subject로 사용)
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(email) // 사용자 이메일을 subject로 설정
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey()) // 알고리즘 자동 선택
                .compact();
    }

    // JWT 토큰에서 이메일(사용자명) 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser() // 0.13.0에서는 parserBuilder → parser
                .verifyWith(getSigningKey()) // 0.13.0 새로운 방식
                .build()
                .parseSignedClaims(token) // 0.13.0에서는 parseClaimsJws → parseSignedClaims
                .getPayload(); // 0.13.0에서는 getBody → getPayload

        return claims.getSubject();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    // JWT 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // 파싱 오류 시 만료된 것으로 간주
        }
    }
}