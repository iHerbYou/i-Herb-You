package com.iherbyou.security.filter;

import com.iherbyou.security.auth.CustomUserDetailsService;
import com.iherbyou.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                if (jwtUtil.validateAccessToken(jwt)) {
                    // 유효한 토큰 - 인증 설정
                    String email = jwtUtil.getEmailFromToken(jwt);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JWT 토큰으로 사용자 인증 완료: {} (URI: {})", email, request.getRequestURI());
                } else {
                    // 만료되거나 유효하지 않은 토큰 - 명시적으로 SecurityContext 초기화
                    log.debug("유효하지 않거나 만료된 토큰 (URI: {})", request.getRequestURI());
                    SecurityContextHolder.clearContext();
                }
            }

        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 오류 발생 (URI: {}): {}", request.getRequestURI(), e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출
     * Authorization: Bearer token 형태에서, "Bearer"을 제외한 토큰 부분만 추출
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거 후 토큰 반환
        }

        return null;
    }

    /**
     * 특정 경로는 JWT 검증을 건너뛸 수 있도록 설정 (선택적)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 공개 API는 JWT 검증 생략 (성능 최적화)
        return path.startsWith("/api/users/login") ||
                path.startsWith("/api/users/signup") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }

}