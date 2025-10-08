package com.iherbyou.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ResetPasswordRequestDto {

    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}
