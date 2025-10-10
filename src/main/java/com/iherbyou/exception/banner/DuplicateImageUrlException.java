package com.iherbyou.exception.banner;

public class DuplicateImageUrlException extends RuntimeException {
    public DuplicateImageUrlException(String imageUrl) {
        super("이미 사용 중인 이미지 URL입니다: " + imageUrl);
    }
}