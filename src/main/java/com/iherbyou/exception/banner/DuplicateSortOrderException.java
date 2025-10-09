package com.iherbyou.exception.banner;

public class DuplicateSortOrderException extends RuntimeException {
    public DuplicateSortOrderException(Integer sortOrder) {
        super("이미 사용 중인 정렬 순서입니다: " + sortOrder);
    }
}