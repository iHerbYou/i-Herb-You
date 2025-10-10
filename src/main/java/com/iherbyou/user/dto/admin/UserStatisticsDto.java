package com.iherbyou.user.dto.admin;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserStatisticsDto {

    // 상태별 회원 수
    private Long totalUsers;        // 전체 회원 수
    private Long activeUsers;       // 활성 회원 수
    private Long inactiveUsers;     // 비활성 회원 수 (이메일 미인증)
    private Long suspendedUsers;    // 정지 회원 수
    private Long deletedUsers;      // 탈퇴 회원 수

    // 기간별 가입자 수
    private Long todaySignups;      // 오늘 가입자
    private Long thisWeekSignups;   // 이번 주 가입자
    private Long thisMonthSignups;  // 이번 달 가입자
}
