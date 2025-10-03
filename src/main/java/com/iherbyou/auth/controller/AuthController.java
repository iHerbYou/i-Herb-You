package com.iherbyou.auth.controller;

import com.iherbyou.user.dto.RefreshTokenRequestDto;
import com.iherbyou.user.dto.RefreshTokenResponseDto;
import com.iherbyou.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;

    @Operation(summary = "토큰 갱신", description = "RefreshToken을 사용하여 새로운 AccessToken 발급")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        RefreshTokenResponseDto response = userService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

}
