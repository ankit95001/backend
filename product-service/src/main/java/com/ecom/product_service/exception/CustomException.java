package com.ecom.product_service.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final int errorCode;
    public CustomException(String message,int errorCode) {
        super(message);
        this.errorCode=errorCode;
    }
}
