package com.ecom.order_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ErrorResponse> handleCartEmpty(CartEmptyException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorResponse> handlePaymentFailed(PaymentFailedException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurity(SecurityException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter type: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorResponse(status.value(), message, LocalDateTime.now()), status);
    }

    record ErrorResponse(int statusCode, String message, LocalDateTime timestamp) {}
}
