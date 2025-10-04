package com.iherbyou.exception.wishlist;

import lombok.Getter;

/**
 * 위시리스트 도메인 예외
 * 코드와 메시지를 함께 전달하여 다양한 예외 상황을 표현
 */
@Getter
public class WishlistException extends RuntimeException {
    private final String code;

    public WishlistException(String code, String message) {
        super(message);
        this.code = code;
    }
}