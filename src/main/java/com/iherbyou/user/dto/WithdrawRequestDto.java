package com.iherbyou.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class WithdrawRequestDto {

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
