package com.iherbyou.exception.catalog;

public class CategoryForbiddenException extends CategoryException {

    // 접근 권한 없음
    public CategoryForbiddenException() {
        super("접근 권한이 없습니다.");
    }

}
