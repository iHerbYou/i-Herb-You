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
    private String accessToken; // JWT Token
    private String tokenType; // Bearer
    private Long expiresIn; // 토큰 만료 시간 (초)
    private String message;

}
