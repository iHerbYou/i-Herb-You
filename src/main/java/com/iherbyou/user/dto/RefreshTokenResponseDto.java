package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;          // "Bearer"
    private Long accessTokenExpiresIn; // Access Token 만료 시간 (초)
    private Long refreshTokenExpiresIn; // Refresh Token 만료 시간 (초)
    private String message;

}
