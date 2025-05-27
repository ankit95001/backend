package com.ecom.cart_service.exception;

public class StockUnavailableException extends RuntimeException {
    public StockUnavailableException(String message) {
        super(message);
    }
}

