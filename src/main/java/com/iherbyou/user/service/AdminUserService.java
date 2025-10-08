package com.iherbyou.user.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.exception.user.UserNotFoundException;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.dto.admin.AdminUserDto;
import com.iherbyou.user.dto.admin.AdminUserListResponseDto;
import com.iherbyou.user.dto.admin.ChangeUserStatusDto;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final CodeService codeService;

    /**
     * 관리자 권한 확인
     */
    @Transactional(readOnly = true)
    public void checkAdminPermission(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!user.isAdmin()) {
            log.warn("관리자 권한 없는 접근 시도: {}", userPrincipal.getEmail());
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }

    /**
     * 회원 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public AdminUserListResponseDto getUserList(UserPrincipal userPrincipal, Pageable pageable) {
        // 관리자 권한 확인
        checkAdminPermission(userPrincipal);

        log.info("회원 목록 조회 - 관리자: {}, 페이지: {}", userPrincipal.getEmail(), pageable.getPageNumber());

        // 회원 목록 조회
        Page<User> users = userRepository.findAll(pageable);

        // DTO 변환
        Page<AdminUserDto> userDtos = users.map(user -> AdminUserDto.from(user));

        return AdminUserListResponseDto.from(userDtos);
    }

    /**
     * 회원 상태 변경 (정지/복구)
     */
    @Transactional
    public void changeUserStatus(UserPrincipal userPrincipal, Long userId, ChangeUserStatusDto request) {

        // 관리자 권한 확인
        checkAdminPermission(userPrincipal);
        log.info("회원 상태 변경 시도 - 관리자: {}, 대상 회원 id: {}, 변경할 상태: {}", userPrincipal.getEmail(), userId, request.getStatusCode());

        // 대상 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("대상 회원을 찾을 수 없습니다."));

        // 상태 코드 조회
        Code newStatus = codeService.getCode(71, request.getStatusCode());
        if (newStatus == null) {
            throw new IllegalArgumentException("유효하지 않은 상태코드입니다.");
        }

        // user 상태 변경
        user.changeUserStatus(newStatus);
        log.info("회원 상태 변경 완료 - 회원: {}, 새상태: {}, 사유: {}", user.getEmail(), newStatus.getDisplayName(), request.getReason());
    }

}