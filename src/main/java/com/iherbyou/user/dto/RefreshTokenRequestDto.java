package com.iherbyou.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefreshTokenRequestDto {

    @NotBlank(message = "refresh token은 필수입니다.")
    private String refreshToken;
}
