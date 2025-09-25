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
            // 1. 요청 헤더에서 JWT 토큰 추출
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                // 2. 토큰에서 사용자 이메일 추출
                String email = jwtUtil.getEmailFromToken(jwt);

                // 3. UserDetails 조회 (DB에서 사용자 정보 로드)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                // 4. Authentication 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 5. 요청 세부 정보 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT 토큰으로 사용자 인증 완료: {} (URI: {})", email, request.getRequestURI());
            }

        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 오류 발생 (URI: {}): {}",
                    request.getRequestURI(), e.getMessage());

            // SecurityContext 초기화 (인증 실패)
            SecurityContextHolder.clearContext();
        }

        // 7. 다음 필터로 요청 전달
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