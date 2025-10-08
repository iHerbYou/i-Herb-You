package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class WithdrawResponseDto {

    private String message;

    public static WithdrawResponseDto success() {
        return WithdrawResponseDto.builder()
                .message("회원 탈퇴가 완료되었습니다. 그동안 iherbyou를 이용해 주셔서 감사합니다.")
                .build();
    }

}
