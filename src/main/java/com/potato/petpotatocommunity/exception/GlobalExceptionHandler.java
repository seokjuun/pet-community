package com.potato.petpotatocommunity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<String> handlePostException(PostException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}