package com.iherbyou.exception.catalog;

public class CategoryNotFoundException extends CategoryException {

    // 카테고리 찾을 수 없음
    public CategoryNotFoundException(Long id) {
        super("카테고리를 찾을 수 없습니다." + ((id != null) ? " (id=" + id + ")" : ""));
    }

}
