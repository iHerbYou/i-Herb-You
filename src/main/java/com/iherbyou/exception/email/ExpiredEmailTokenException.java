package com.iherbyou.exception.email;

public class ExpiredEmailTokenException extends RuntimeException {
    public ExpiredEmailTokenException(String message) {
        super(message);
    }
}
