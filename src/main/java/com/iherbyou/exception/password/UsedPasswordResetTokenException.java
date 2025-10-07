package com.iherbyou.exception.password;

public class UsedPasswordResetTokenException extends RuntimeException {
    public UsedPasswordResetTokenException(String message) {
        super(message);
    }
}
