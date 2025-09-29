package com.iherbyou.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LoginResponseDto { //TODO

    private String email;
    private String name;
    private String accessToken; // JWT Access Token
    private String refreshToken; // JWT Refresh Token
    private String tokenType; // Bearer
    private Long accessTokenExpiresIn; // Access Token 만료 시간 (초)
    private Long refreshTokenExpiresIn;  // Refresh Token 만료 시간 (초)
    private String message;

}
