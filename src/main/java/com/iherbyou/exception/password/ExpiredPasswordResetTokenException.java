package com.iherbyou.exception.password;

public class ExpiredPasswordResetTokenException extends RuntimeException {
    public ExpiredPasswordResetTokenException(String message) {
        super(message);
    }
}
