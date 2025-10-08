package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ResetPasswordResponseDto {

    private String message;

    public static ResetPasswordResponseDto requestSuccess() {
        return ResetPasswordResponseDto.builder()
                .message("비밀번호 재설정 이메일이 발송되었습니다. 이메일을 확인해주세요.")
                .build();
    }

    public static ResetPasswordResponseDto resetSuccess() {
        return ResetPasswordResponseDto.builder()
                .message("비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해주세요.")
                .build();
    }
}