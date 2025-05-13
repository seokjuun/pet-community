package com.potato.petpotatocommunity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private String result;  // success 또는 fail
    private T data;         // 응답 데이터
    private String message; // 메시지

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>("success", data, null);
    }

    public static <T> ResponseDto<T> success(T data, String message) {
        return new ResponseDto<>("success", data, message);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>("fail", null, message);
    }
}