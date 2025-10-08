package com.iherbyou.user.controller;

import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.dto.*;
import com.iherbyou.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    // 회원가입 (signup)
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto request) {
        SignUpResponseDto response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 이메일 인증
    @Operation(summary = "이메일 인증", description = "이메일로 받은 토큰으로 계정을 활성화합니다.")
    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponseDto> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(EmailVerificationResponseDto.success());
    }

    // 이메일 재발송
    @PostMapping("/resend-verification")
    public ResponseEntity<EmailVerificationResponseDto> resendVerification(@RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok(EmailVerificationResponseDto.resendSuccess());
    }

    // 비밀번호 재설정 요청
    @PostMapping("/password-reset-request")
    public ResponseEntity<ResetPasswordResponseDto> requestPasswordReset(@Valid @RequestBody ResetPasswordRequestDto request) {
        ResetPasswordResponseDto response = userService.requestResetPassword(request);
        return ResponseEntity.ok(response);
    }

    // 비밀번호 재설정 확인
    @PostMapping("/password-reset-confirm")
    public ResponseEntity<ResetPasswordResponseDto> resetPassword(@RequestParam String token, @Valid @RequestBody ResetPasswordConfirmDto request) {
        ResetPasswordResponseDto response = userService.resetPassword(token, request);
        return ResponseEntity.ok(response);
    }

    // 로그인 (login)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // 로그아웃 (logout)
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        LogoutResponseDto response = userService.logout(userPrincipal);
        return ResponseEntity.ok(response);
    }

    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserInfoResponseDto response = userService.getCurrentUser(userPrincipal);
        return ResponseEntity.ok(response);
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordRequestDto request) {
        ChangePasswordResponseDto response = userService.changePassword(request, userPrincipal);
        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "비밀번호 확인 후 계정을 탈퇴처리 (soft delete)")
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponseDto> withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody WithdrawRequestDto request) {
        WithdrawResponseDto response = userService.withdraw(userPrincipal, request);
        return ResponseEntity.ok(response);
    }

}
