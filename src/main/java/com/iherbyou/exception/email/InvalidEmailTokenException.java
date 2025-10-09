package com.iherbyou.exception.email;

public class InvalidEmailTokenException extends RuntimeException {
    public InvalidEmailTokenException(String message) {
        super(message);
    }
}
