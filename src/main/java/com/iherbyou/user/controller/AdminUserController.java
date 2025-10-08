package com.iherbyou.user.controller;

import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.dto.admin.AdminUserListResponseDto;
import com.iherbyou.user.dto.admin.ChangeUserStatusDto;
import com.iherbyou.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - User Management", description = "관리자 전용 회원 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
@RestController
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 회원 목록 조회
    @Operation(summary = "회원 목록 조회", description = "전체 회원 목록을 페이징하여 조회합니다 (관리자 전용)")
    public ResponseEntity<AdminUserListResponseDto> getUserList(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        AdminUserListResponseDto response = adminUserService.getUserList(userPrincipal, pageable);
        return ResponseEntity.ok(response);
    }

    // 회원 상태 변경
    // 회원 상태 변경
    @Operation(summary = "회원 상태 변경", description = "회원의 상태를 변경합니다 (정지/복구 등) (관리자 전용).")
    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> changeUserStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserStatusDto request) {
        adminUserService.changeUserStatus(userPrincipal, userId, request);
        return ResponseEntity.ok().build();
    }
}
