package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EmailVerificationResponseDto {

    private String message;

    public static EmailVerificationResponseDto success() {
        return EmailVerificationResponseDto.builder()
                .message("이메일 인증이 완료되었습니다. 로그인해주세요.")
                .build();
    }

    public static EmailVerificationResponseDto resendSuccess() {
        return EmailVerificationResponseDto.builder()
                .message("인증 이메일이 재발송 되었습니다.")
                .build();
    }

}
