package com.ecom.order_service.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(String msg) { super(msg); }
}
