package com.iherbyou.exception.catalog;

public class RestockSubscriptionNotFoundException extends RuntimeException {

    public RestockSubscriptionNotFoundException(Long id) {
        super("Restock subscription not found with id: " + id);
    }
    
}
