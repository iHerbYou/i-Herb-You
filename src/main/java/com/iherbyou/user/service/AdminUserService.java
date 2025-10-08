package com.iherbyou.user.service;

import com.iherbyou.exception.user.UserNotFoundException;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final UserRepository userRepository;

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
}