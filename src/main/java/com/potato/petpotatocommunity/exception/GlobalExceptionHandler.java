package com.potato.petpotatocommunity.exception;

import com.potato.petpotatocommunity.dto.user.LoginResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 로그인 실패 예외 처리
//    @ExceptionHandler(LoginFailedException.class)
//    public ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException e) {
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.UNAUTHORIZED.value(),
//                "Unauthorized",
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public LoginResultDto handleLoginFailedException(LoginFailedException ex) {
        return new LoginResultDto("fail", null);  // DTO 형태 유지
    }

    // 비밀번호 관련 예외 처리
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Map<String, String>> handlePasswordException(PasswordException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 사용자를 찾을 수 없는 경우 예외 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    // 모든 예외 처리 (디버깅용)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다: " + e.getMessage());
    }
}

