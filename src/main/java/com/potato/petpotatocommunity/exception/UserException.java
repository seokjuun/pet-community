package com.potato.petpotatocommunity.exception;

public class UserException extends RuntimeException {
    public static class UserNotFoundException extends BusinessException {
        public UserNotFoundException(String email) {
            super("USER_NOT_FOUND", String.format("Email %s인 회원을 찾을 수 없습니다.", email));
        }

        public UserNotFoundException(Long userId) {
            super("USER_NOT_FOUND", String.format("ID가 %d인 회원을 찾을 수 없습니다.", userId));
        }
    }
}
