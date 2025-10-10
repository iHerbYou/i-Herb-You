package com.iherbyou.common.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;     // ex) VARIANT_NOT_FOUND, DUPLICATE_WISHLIST_ITEM, UNAUTHORIZED
    private String message;  // 사용자/개발자 메시지
}