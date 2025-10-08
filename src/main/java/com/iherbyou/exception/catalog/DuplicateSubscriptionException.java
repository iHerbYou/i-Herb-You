package com.iherbyou.exception.catalog;

public class DuplicateSubscriptionException extends RuntimeException {

    public DuplicateSubscriptionException() {
        super("이미 재입고 알림을 신청한 상품입니다.");
    }

    public DuplicateSubscriptionException(String message) {
        super(message);
    }

}