package com.iherbyou.exception.catalog;

public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(Long id) {
        super("재입고 알림 구독을 찾을 수 없습니다. id=" + id);
    }

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}