package com.potato.petpotatocommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int status;             // HTTP 상태 코드
    private String error;           // 에러 유형
    private String message;         // 에러 메시지
    private LocalDateTime timestamp; // 발생 시간

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
