package com.iherbyou.exception.catalog;

public class CategoryUnauthorizedException extends CategoryException {

    // 인증이 필요할 때
    public CategoryUnauthorizedException() {
        super("인증이 필요합니다.");
    }

}
