package com.iherbyou.user.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.exception.user.*;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.security.jwt.JwtUtil;
import com.iherbyou.user.dto.*;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CodeService codeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // JWT Utility 추가

    @Value("${jwt.expiration:86400000}") // JWT 만료 시간 (기본 24시간)
    private Long jwtExpirationMs;

    /**
     * 회원가입 (SignUp)
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        log.info("signUp requestDto: {}", request.getEmail());

        // 중복 email 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }

        // 전화번호 중복 확인 (전화번호가 있는 경우)
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()
                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("이미 사용 중인 전화번호입니다:" + request.getPhoneNumber());
        }

        // 기본 사용자 권한 조회 (그룹 70, 코드 701 = USER) -> DB에 필수 기준 데이터가 있는지 확인하는 안전장치
        Code defaultUserRole = codeService.getDefaultUserRole();
        if (defaultUserRole == null) {
            throw new IllegalStateException("기본 사용자 권한을 찾을 수 없습니다");
        }

        // 기본 사용자 상태 조회 (그룹 71, 코드 712 = ACTIVE) -> DB에 필수 기준 데이터가 있는지 확인하는 안전장치
        Code defaultUserStatus = codeService.getDefaultActiveStatus();
        if (defaultUserStatus == null) {
            throw new IllegalStateException("기본 사용자 상태를 찾을 수 없습니다");
        }

        // User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .roleCode(defaultUserRole)
                .statusCode(defaultUserStatus)
                .build();

        // 저장
        User savedUser = userRepository.save(user);

        log.info("회원가입 성공: {} (ID: {}) - 권한: {}, 상태: {}",
                savedUser.getEmail(), savedUser.getId(),
                savedUser.getRoleName(), savedUser.getStatusName());

        // 반환
        return SignUpResponseDto.builder()
                .email(savedUser.getEmail())
                .message("회원가입이 완료되었습니다. 로그인 해주세요.")
                .build();
    }

    /**
     * 로그인 (Login) - JWT 토큰 생성해서 반환
     */
    public LoginResponseDto login(LoginRequestDto request) {
        log.info("로그인 시도: {}", request.getEmail());

        // 활성 사용자만 조회
        User user = userRepository.findActiveUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없거나 비활성 상태입니다"));

        // 암호화된 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치: {}", request.getEmail());
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다");
        }

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateToken(user.getEmail());

        // 로그인 성공
        log.info("로그인 성공: {} (id: {}) - jwt token 생성 완료", user.getEmail(), user.getId());

        return LoginResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationMs / 1000) // 초 단위로 변환
                .message("로그인 성공")
                .build();
    }

    /**
     * 로그아웃 (Logout)
     */
    public LogoutResponseDto logout(UserPrincipal userPrincipal) {
        log.info("logout request: {} (id: {}", userPrincipal.getEmail(), userPrincipal.getId());
        // 서버에서는 별도 처리 없이 응답만 반환 -> client 에서 토큰을 제거해야함
        log.info("로그아웃 완료: {}", userPrincipal.getEmail());
        return LogoutResponseDto.success();
    }

}