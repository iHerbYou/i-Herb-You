package com.iherbyou.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ChangePasswordRequestDto {

    @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호 입력은 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
    private String newPassword;

}
