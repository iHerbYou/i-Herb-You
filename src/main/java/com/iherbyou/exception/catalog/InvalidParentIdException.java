package com.iherbyou.exception.catalog;

public class InvalidParentIdException extends CategoryException {

    // 잘못된 parentId 요청
    public InvalidParentIdException(Long parentId) {
        super("유효하지 않은 parentId입니다. (parentId=" + parentId + ")");
    }

}
