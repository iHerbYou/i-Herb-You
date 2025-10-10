package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ChangePasswordResponseDto {

    private String message;

    public static ChangePasswordResponseDto success() {
        return ChangePasswordResponseDto.builder()
                .message("비밀번호가 성공적으로 변경되었습니다.")
                .build();
    }

}
