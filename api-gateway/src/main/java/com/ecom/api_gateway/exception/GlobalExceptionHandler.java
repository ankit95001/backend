package com.ecom.api_gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class    GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllOtherExceptions(RuntimeException ex) {
        return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

