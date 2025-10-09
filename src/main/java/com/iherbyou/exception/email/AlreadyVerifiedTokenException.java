package com.iherbyou.exception.email;

public class AlreadyVerifiedTokenException extends RuntimeException {
    public AlreadyVerifiedTokenException(String message) {
        super(message);
    }
}
