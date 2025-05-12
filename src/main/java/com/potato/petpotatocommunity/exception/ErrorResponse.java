package com.potato.petpotatocommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;             // HTTP 상태 코드
    private String error;           // 에러 유형
    private String message;         // 에러 메시지
    private LocalDateTime timestamp; // 발생 시간
}
