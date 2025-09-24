package com.iherbyou.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 (일단 비활성화 - API 테스트를 위해)
                .csrf(csrf -> csrf.disable())

                // 요청별 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인 관련은 모든 사용자 접근 허용
                        .requestMatchers("/api/user/signup", "/api/user/login").permitAll()
                        // 코드 조회 API는 모든 사용자 접근 허용 (일단)
                        .requestMatchers("/api/codes/**").permitAll()
                        // 정적 리소스 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // 폼 로그인 비활성화 (REST API 사용)
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

}