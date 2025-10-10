package com.iherbyou.exception.Promotion;

public class PointInsufficientBalanceException extends RuntimeException {

    public PointInsufficientBalanceException(Long userId, int balance, int requested) {
        super(String.format("포인트 잔액이 부족합니다. userId=%s, balance=%d, requested=%d",
                userId != null ? userId : "anonymous",
                balance,
                requested));
    }
}

