package com.iherbyou.user.dto.admin;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 관리자가 사용자 목록 조회 시 전체 응답 (목록 + 페이징 정보)
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AdminUserListResponseDto {

    private List<AdminUserDto> users; // 사용자 목록
    private int totalElements; // 전체 사용자
    private int totalPages; // 전체 페이지 수
    private int currentPage; // 현재 페이지 번호
    private int pageSize; // 페이지 크기

    public static AdminUserListResponseDto from(Page<AdminUserDto> page) {
        return AdminUserListResponseDto.builder()
                .users(page.getContent())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
