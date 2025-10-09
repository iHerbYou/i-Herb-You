package com.iherbyou.user.service;

import com.iherbyou.common.code.entity.Code;
import com.iherbyou.common.code.service.CodeService;
import com.iherbyou.exception.user.UserNotFoundException;
import com.iherbyou.security.auth.UserPrincipal;
import com.iherbyou.user.dto.admin.*;
import com.iherbyou.user.entity.User;
import com.iherbyou.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    /**
     * 회원 검색 (페이징)
     */
    @Transactional
    public AdminUserListResponseDto searchUsers(UserPrincipal userPrincipal, UserSearchDto searchDto, Pageable pageable) {

        // 관리자 권한 확인
        checkAdminPermission(userPrincipal);
        log.info("회원 검색 - 관리자: {}, 검색 타입: {}, 키워드: {}", userPrincipal.getEmail(), searchDto.getSearchType(), searchDto.getKeyword());

        // 검색 타입에 따라 검색
        Page<User> users;
        switch (searchDto.getSearchType().toLowerCase()) {
            case "email":
                users = userRepository.searchByEmail(searchDto.getKeyword(), pageable);
                break;
            case "name":
                users = userRepository.searchByName(searchDto.getKeyword(), pageable);
                break;
            case "phone":
                users = userRepository.searchByPhone(searchDto.getKeyword(), pageable);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 검색 타입입니다. (email, name, phone 중 선택)");
        }

        // DTO 변환
        Page<AdminUserDto> userDtos = users.map(user -> AdminUserDto.from(user));

        return AdminUserListResponseDto.from(userDtos);
    }

    /**
     * 회원 상세 조회
     */
    @Transactional(readOnly = true)
    public AdminUserDetailDto getUserDetail(UserPrincipal userPrincipal, Long userId) {

        // 관리자 권한 확인
        checkAdminPermission(userPrincipal);

        log.info("회원 상세 조회 - 관리자: {}, 대상 회원 ID: {}", userPrincipal.getEmail(), userId);

        // 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        return AdminUserDetailDto.from(user);
    }

    /**
     * 회원 통계 조회
     */
    @Transactional(readOnly = true)
    public UserStatisticsDto getUserStatistics(UserPrincipal userPrincipal) {

        // 관리자 권한 확인
        checkAdminPermission(userPrincipal);

        log.info("회원 통계 조회 - 관리자: {}", userPrincipal.getEmail());

        // 상태별 회원 수
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByStatusCode(712);      // ACTIVE
        Long inactiveUsers = userRepository.countByStatusCode(711);    // INACTIVE
        Long suspendedUsers = userRepository.countByStatusCode(713);   // SUSPENDED
        Long deletedUsers = userRepository.countByStatusCode(715);     // DELETED

        // 기간별 가입자 수
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.minusWeeks(1);
        LocalDateTime startOfMonth = now.minusMonths(1);

        Long todaySignups = userRepository.countByCreatedAtAfter(startOfToday);
        Long thisWeekSignups = userRepository.countByCreatedAtAfter(startOfWeek);
        Long thisMonthSignups = userRepository.countByCreatedAtAfter(startOfMonth);

        return UserStatisticsDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .suspendedUsers(suspendedUsers)
                .deletedUsers(deletedUsers)
                .todaySignups(todaySignups)
                .thisWeekSignups(thisWeekSignups)
                .thisMonthSignups(thisMonthSignups)
                .build();
    }

}