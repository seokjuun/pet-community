package com.potato.petpotatocommunity.exception;

import com.potato.petpotatocommunity.dto.post.PostDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(PostException.class)
//    public ResponseEntity<String> handlePostException(PostException e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<PostDetailResponse> handlePostException(PostException e) {
        return ResponseEntity.badRequest().body(
                PostDetailResponse.builder()
                        .result("fail")
                        .title(e.getMessage())
                        .build()
        );
    }

}