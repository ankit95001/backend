package com.ecom.api_gateway.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}

