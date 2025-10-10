package com.iherbyou.user.dto.admin;

import lombok.*;

/**
 * 관리자가 회원을 검색할 때 사용하는 검색 조건
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserSearchDto {

    private String keyword;      // 검색어
    private String searchType;   // 검색 타입: email, name, phone
}