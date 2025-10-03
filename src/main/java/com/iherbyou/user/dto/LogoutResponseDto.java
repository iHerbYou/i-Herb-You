package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LogoutResponseDto {

    private String message;

    public static LogoutResponseDto success() {
        return LogoutResponseDto.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();
    }

}
