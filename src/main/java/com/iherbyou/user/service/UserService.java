package com.iherbyou.user.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.exception.email.AlreadyVerifiedTokenException;
import com.iherbyou.exception.email.ExpiredEmailTokenException;
import com.iherbyou.exception.email.InvalidEmailTokenException;
import com.iherbyou.exception.user.*;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.security.jwt.JwtUtil;
import com.iherbyou.user.dto.*;
import com.iherbyou.user.entity.EmailVerificationToken;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.EmailVerificationTokenRepository;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CodeService codeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // JWT Utility 추가
    private final EmailService emailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

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

        // 기본 사용자 상태 조회 (그룹 71, 코드 711 = INACTIVE) -> DB에 필수 기준 데이터가 있는지 확인하는 안전장치
        Code inactiveStatus = codeService.getCode(71, 711); // 이메일 인증 전이므로 inactive 상태
        if (inactiveStatus == null) {
            throw new IllegalStateException("비활성 상태 코드를 찾을 수 없습니다.");
        }

        // User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .roleCode(defaultUserRole)
                .statusCode(inactiveStatus) // 비활성화 유저 (이메일 인증 전)
                .build();

        // 저장
        User savedUser = userRepository.save(user);

        // 이메일 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiresAt(LocalDateTime.now().plusHours(24)) // 24시간 이후 토큰 만료 (이메일 인증 기한)
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        // 이메일 발송
        emailService.sendVerificationEmail(savedUser.getEmail(), token);

        log.info("회원가입 성공: {} (ID: {}) - 이메일 인증 메일 발송", savedUser.getEmail(), savedUser.getId());

        // 반환
        return SignUpResponseDto.builder()
                .email(savedUser.getEmail())
                .message("회원가입이 완료되었습니다. 이메일을 확인하여 인증을 완료해주세요.")
                .build();
    }

    /**
     * 이메일 인증 (회원가입 과정)
     */
    @Transactional
    public void verifyEmail(String token) {
        log.info("이메일 인증 시도: token={}", token.substring(0, 10) + "...");

        // 토큰 조회
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidEmailTokenException("유효하지 않거나 이미 사용된 토큰입니다."));

        // 이미 인증된 토큰인지 확인
        if (verificationToken.isVerified()) {
            throw new AlreadyVerifiedTokenException("이미 인증된 토큰입니다.");
        }

        // 토큰 만료 확인
        if (verificationToken.isExpired()) {
            throw new ExpiredEmailTokenException("만료된 토큰입니다. 재발송을 요청해주세요.");
        }

        // 토큰 인증 완료 처리
        verificationToken.verify();

        // 사용자 계정 활성화 (INACTIVE -> ACTIVE)
        User user = verificationToken.getUser();
        Code activeStatus = codeService.getCode(71, 712);// ACTIVE
        if (activeStatus == null) {
            throw new IllegalStateException("활성 상태 코드를 찾을 수 없습니다.");
        }
        user.changeUserStatus(activeStatus); // 유저 상태 ACTIVE로 변경
        log.info("이메일 인증 완료: {}", user.getEmail());

        // 토큰 삭제 (인증 끝났으니 DB에서 제거 -> 재사용 공격 방지 가능)
        emailVerificationTokenRepository.delete(verificationToken);
        log.info("이메일 인증 완료 및 토큰 삭제: {} (token: {}...)", user.getEmail(), token.substring(0, 10));
    }

    /**
     * 이메일 인증 재발송
     */
    @Transactional
    public void resendVerificationEmail(String email) {
        log.info("이메일 인증 재발송 요청: {}", email);

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 이미 활성화된 계정인지 확인
        if (user.isActive()) {
            throw new IllegalStateException("이미 인증된 계정입니다.");
        }

        // 기존 토큰 삭제
        emailVerificationTokenRepository.deleteByUser(user);

        // 새 토큰 생성
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        // 이메일 재발송
        emailService.sendVerificationEmail(user.getEmail(), token);

        log.info("이메일 인증 재발송 완료: {}", email);
    }


    /**
     * 로그인 (Login) - JWT 토큰 생성해서 반환
     */
    @Transactional(readOnly = true)
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
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // 로그인 성공
        log.info("로그인 성공: {} (id: {}) - jwt token 생성 완료", user.getEmail(), user.getId());

        return LoginResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtUtil.getAccessTokenExpirationInSeconds()) // 초 단위로 변환
                .refreshTokenExpiresIn(jwtUtil.getRefreshTokenExpirationInSeconds())
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

    /**
     * 토큰 갱신 (Refresh token 으로 새로운 Access Token 발급)
     */
    @Transactional(readOnly = true)
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();
        log.info("토큰 갱신 요청 - Refresh Token: {}...", refreshToken.substring(0, 20));

        // refresh token 유효성 검증
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            log.warn("토큰 갱신 실패 - 유효하지 않은 Refresh Token");
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");
        }

        // Refresh Token에서 사용자 이메일 추출
        String userEmail = jwtUtil.getEmailFromToken(refreshToken);

        // 활성 사용자 확인
        User user = userRepository.findActiveUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없거나 비활성 상태입니다."));

        // 새로운 토큰들 생성, 반환
        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return RefreshTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtUtil.getAccessTokenExpirationInSeconds())
                .refreshTokenExpiresIn(jwtUtil.getRefreshTokenExpirationInSeconds())
                .message("토큰 갱신 성공")
                .build();
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public UserInfoResponseDto getCurrentUser(UserPrincipal userPrincipal) {
        log.info("사용자 정보 조회: {}", userPrincipal.getEmail());

        // DB에서 최신 사용자 정보 조회
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        return UserInfoResponseDto.from(user);
    }

    /**
     * 비밀번호 변경 (Change Password)
     */
    @Transactional
    public ChangePasswordResponseDto changePassword(ChangePasswordRequestDto request, UserPrincipal userPrincipal) {
        log.info("비밀번호 변경 시도: {}", userPrincipal.getEmail());

        // 사용자 조회
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 사용자의 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.warn("비밀번호 변경 실패 - 현재 비밀번호 불일치: {}", userPrincipal.getEmail());
            throw new InvalidPasswordException("비밀번호가 틀렸습니다.");
        }

        // 현재 비밀번호와 새 비밀번호가 일치하는지 확인
        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야합니다.");
        }

        // 비밀번호 변경
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("비밀번호 변경 성공: {}", userPrincipal.getEmail());

        return ChangePasswordResponseDto.success();
    }

}