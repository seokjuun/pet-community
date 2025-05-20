package com.potato.petpotatocommunity.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("로그인에 실패했습니다.");
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
