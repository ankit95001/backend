package com.ecom.order_service.exception;

public class MessageSendException extends RuntimeException {
    public MessageSendException(String msg) { super(msg); }
}
