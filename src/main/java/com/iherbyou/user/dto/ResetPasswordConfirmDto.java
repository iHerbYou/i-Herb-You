package com.iherbyou.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ResetPasswordConfirmDto {

    // 토큰은 url query parameter로 받는다. 사용자는 비밀번호만 입력하면 됨.
    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String newPassword;
}
